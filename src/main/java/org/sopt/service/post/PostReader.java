package org.sopt.service.post;

import java.util.Optional;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.response.post.PostPreviewResponse;
import org.sopt.dto.response.post.PostPreviewResponses;
import org.sopt.dto.response.post.PostResponse;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.post.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PostReader {
	private final PostRepository postRepository;

	public PostReader(final PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public Post findById(final Long id) {
		return postRepository.findById(id).orElseThrow(()
			-> new NotFoundException(ErrorCode.POST_NOT_FOUND));
	}

	public PostResponse getPost(final Long id) {
		Post post = postRepository.findByIdWithUser(id).orElseThrow(()
			-> new NotFoundException(ErrorCode.POST_NOT_FOUND));
		return PostResponse.from(post);
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
