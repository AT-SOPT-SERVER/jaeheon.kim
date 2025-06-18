package org.sopt.service.post;

import static org.sopt.constant.CacheConstant.*;

import java.util.Optional;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.response.post.PostPreviewResponse;
import org.sopt.dto.response.post.PostPreviewResponses;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.post.PostRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PostReader {
	private final PostRepository postRepository;
	private final PostCacheService postCacheService;

	public PostReader(final PostRepository postRepository, PostCacheService postCacheService) {
		this.postRepository = postRepository;
		this.postCacheService = postCacheService;
	}

	/**
	 * 만약, 해당 메서드를 서비스에서 private 메서드로 가졌다면 캐싱이 동작하지 않음. self-invocation(자기 호출) 때문.
	 * 이런 이유로 현재 구조는 트랜잭션이나 캐싱 등을 Service의 layer를 한 층 더 나누어 reader와 writer를 사용한 구조임.
	 */
	@Cacheable(cacheNames = POST_CACHE_NAME, key = POST_CACHE_KEY)
	public Post findById(final Long postId) {
		return postRepository.findById(postId).orElseThrow(()
			-> new NotFoundException(ErrorCode.POST_NOT_FOUND));
	}

	public Post findByIdWithRelationInitialized(final Long postId) { // aop로 리팩토링하기
		Optional<Post> optionalPost = postCacheService.getCachedAllRelationInitializedPost(postId);

		Post post = optionalPost.orElseGet(() -> postRepository.findByIdWithUserAndPostTags(postId).orElseThrow(()
			-> new NotFoundException(ErrorCode.POST_NOT_FOUND)));

		postCacheService.putPost(post);
		return post;
	}

	public PostPreviewResponses getPosts(
		final Optional<User> user,
		final Optional<String> keyword,
		final String target,
		final Optional<Long> tagId,
		int page,
		int size) {
		Page<PostPreviewResponse> posts = postRepository.searchPosts(keyword, target, tagId, page, size, user);

		return new PostPreviewResponses(posts);
	}

}
