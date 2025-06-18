package org.sopt.service.post;

import static org.sopt.constant.CacheConstant.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.Tag;
import org.sopt.domain.User;
import org.sopt.dto.request.post.PostCreateRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.post.PostPreviewResponses;
import org.sopt.dto.response.post.PostResponse;
import org.sopt.exception.ConflictException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.service.comment.CommentReader;
import org.sopt.service.tag.TagReader;
import org.sopt.service.tag.TagWriter;
import org.sopt.service.user.UserReader;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

	private final PostReader postReader;
	private final PostWriter postWriter;
	private final UserReader userReader;
	private final TagReader tagReader;
	private final TagWriter tagWriter;
	private final CommentReader commentReader;
	private final ThreadPoolTaskExecutor taskExecutor;

	public PostService(
		final PostReader postReader, final PostWriter postWriter,
		final UserReader userReader,
		final TagReader tagReader, final TagWriter tagWriter,
		final CommentReader commentReader, ThreadPoolTaskExecutor taskExecutor
	) {
		this.postWriter = postWriter;
		this.postReader = postReader;
		this.userReader = userReader;
		this.tagReader = tagReader;
		this.tagWriter = tagWriter;
		this.commentReader = commentReader;
		this.taskExecutor = taskExecutor;
	}

	public PostResponse getPostById(final Long postId) {
		CompletableFuture<Post> postFuture = CompletableFuture.supplyAsync(
			() -> postReader.findByIdWithRelationInitialized(postId), taskExecutor);
		CompletableFuture<List<Comment>> commentsFuture = CompletableFuture.supplyAsync(
			() -> commentReader.findAllByPostId(postId), taskExecutor);

		return CompletableFuture.allOf(postFuture, commentsFuture)
			.thenApply((nothing) -> PostResponse.from(postFuture.join(), commentsFuture.join()))
			.join();
	}

	public PostPreviewResponses getPosts(
		final Optional<Long> userId,
		final Optional<String> keyword,
		final String target,
		final Optional<Long> tagId,
		int page,
		int size) {
		return postReader.getPosts(userId.map(userReader::findById),
			keyword, target, tagId, page, size);
	}

	@Transactional
	@CachePut(cacheNames = POST_CACHE_NAME, key = "#post.id")
	public Post createPost(final Long userId, final PostCreateRequest request) {
		User user = userReader.findById(userId);

		Post post = postIntegritySupplier(() -> postWriter.create(user,
			request.title(),
			request.content()
		));

		List<Tag> tags = tagReader.findAllByIds(request.tagIds());
		tagWriter.createPostTag(post, tags);
		return post;
	}

	@Transactional
	@CacheEvict(cacheNames = POST_CACHE_NAME, key = POST_CACHE_KEY)
	public void deletePostById(final Long postId, final Long userId) {
		Post post = postReader.findById(postId);
		User requestUser = userReader.findById(userId);

		requestUser.checkIsWriter(post.getUser(), ErrorCode.NOT_ALLOWED_POST);

		postWriter.delete(post);
	}

	@Transactional
	@CachePut(cacheNames = POST_CACHE_NAME, key = POST_CACHE_KEY)
	public Post updatePostById(
		final Long postId,
		final PostUpdateRequest request,
		final Long userId
	) {
		Post post = postReader.findById(postId);
		User requestUser = userReader.findById(userId);

		requestUser.checkIsWriter(post.getUser(), ErrorCode.NOT_ALLOWED_POST);

		return postIntegritySupplier(() -> postWriter.update(post, request));
	}

	/**
	 * 함수형 인터페이스를 사용하여 post 쓰기 작업 시 발생하는 무결성 오류를 처리함.
	 * uk_title 인덱스로 title 관련 중복이 발생할 경우 이를 catch하여 사용자에게 POST_TITLE_CONFLICT 을 전달해줌
	 *
	 */
	private <T> T postIntegritySupplier(Supplier<T> supplier) {
		try {
			return supplier.get();
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("title")) {
				throw new ConflictException(ErrorCode.POST_TITLE_CONFLICT);
			}
			throw e;
		}
	}

}
