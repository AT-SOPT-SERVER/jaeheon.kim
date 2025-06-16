package org.sopt.dto.response.post;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.domain.PostTag;
import org.sopt.domain.Tag;

public record PostResponse(Long id,
						   String title,
						   Long writerId,
						   String writerName,
						   String content,
						   List<Tag> tags // dto로 반환하도록 리팩토링 필요

) {
	public static PostResponse from(Post post) {
		return new PostResponse(post.getId(),
			post.getTitle(),
			post.getUser().getId(),
			post.getUser().getName(),
			post.getContent(),
			post.getPostTags().stream()
				.map(PostTag::getTag)
				.toList());
	}

	public record postTags() {

	}
}
