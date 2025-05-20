package org.sopt.dto.response.post;

import org.sopt.domain.Post;

public record PostResponse(Long id,
                           String title,
                           Long writerId,
                           String writerName,
                           String content
) {
    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(),
                post.getTitle(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getContent());
    }
}
