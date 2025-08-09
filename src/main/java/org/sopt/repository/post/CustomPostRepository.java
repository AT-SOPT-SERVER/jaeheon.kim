package org.sopt.repository.post;

import java.util.Optional;

import org.sopt.domain.User;
import org.sopt.dto.response.post.PostPreviewResponse;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
	Page<PostPreviewResponse> searchPosts(
		Optional<String> keyword,
		String target,
		Optional<Long> tagId,
		int page,
		int size,
		Optional<User> user
	);
}
