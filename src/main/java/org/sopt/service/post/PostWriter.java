package org.sopt.service.post;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.repository.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostWriter {
	private final PostRepository postRepository;

	public PostWriter(final PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public Post create(final User user, String title, String content) {
		Post post = Post.createNew(user, title, content);
		return postRepository.save(post);
	}

	@Transactional
	public Post update(final Post post, final PostUpdateRequest request) {
		if (request.title().isPresent()) {
			post.updateTitle(request.title().get());
		}
		if (request.content().isPresent()) {
			post.updateContent(request.content().get());
		}

		return post;
	}

	public void delete(final Post post) {
		postRepository.delete(post);
	}

}
