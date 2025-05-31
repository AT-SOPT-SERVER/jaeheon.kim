package org.sopt.service.post;

import java.util.Optional;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.domain.enums.Tag;
import org.sopt.dto.request.post.PostCreateRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.post.PostPreviewResponses;
import org.sopt.dto.response.post.PostResponse;
import org.sopt.exception.ConflictException;
import org.sopt.exception.ForbiddenException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.service.user.UserReader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@Service
public class PostService {

	private final EntityManager entityManager;

	private final PostReader postReader;
	private final PostWriter postWriter;
	private final UserReader userReader;

	public PostService(EntityManager entityManager, final PostWriter postWriter,
		final PostReader postReader,
		final UserReader userReader) {
		this.entityManager = entityManager;
		this.postWriter = postWriter;
		this.postReader = postReader;
		this.userReader = userReader;
	}

	public PostResponse getPostById(final Long id) {
		return postReader.getPost(id);
	}

	public PostPreviewResponses getPosts(final Optional<String> keyword,
		final String target,
		final Optional<String> tag) {
		return postReader.getPosts(keyword, target, tag.map(Tag::resolveTag));
	}

	public void createPost(final Long userId, final PostCreateRequest request) {
		User user = userReader.findById(userId);
		postIntegrityRunnable(() -> postWriter.create(user,
			request.title(),
			request.content(),
			request.tag().map(Tag::resolveTag)
		));
	}

	public void deletePostById(final Long postId, final Long userId) {
		Post post = postReader.findById(postId);
		User requestUser = userReader.findById(userId);

		checkWriterIsUser(post.getUser(), requestUser);

		postWriter.delete(post);
	}

	@Transactional
	public void updatePostById(
		final Long id,
		final PostUpdateRequest request,
		final Long userId) {
		Post post = postReader.findById(id);
		User requestUser = userReader.findById(userId);

		boolean isManaged = entityManager.contains(post);

		checkWriterIsUser(post.getUser(), requestUser);

		postIntegrityRunnable(() -> postWriter.update(post, request));

		boolean isManaged2 = entityManager.contains(post);

		System.out.print(isManaged2);

	}

	/**
	 * 함수형 인터페이스를 사용하여 post 쓰기 작업 시 발생하는 무결성 오류를 처리함.
	 * uk_title 인덱스로 title 관련 중복이 발생할 경우 이를 catch하여 사용자에게 POST_TITLE_CONFLICT 을 전달해줌
	 *
	 * @param runnable
	 */
	private void postIntegrityRunnable(Runnable runnable) {
		try {
			runnable.run();
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("title")) {
				throw new ConflictException(ErrorCode.POST_TITLE_CONFLICT);
			}
			throw e;
		}
	}

	private void checkWriterIsUser(User writer, User user) {
		if (!user.equals(writer)) {
			throw new ForbiddenException(ErrorCode.NOT_ALLOWED_POST);
		}
	}
}
