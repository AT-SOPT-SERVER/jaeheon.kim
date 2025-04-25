package org.sopt.dto.response;

import org.sopt.domain.Post;

import java.util.List;

public record PostResponses(List<PostResponse> responseList) {
    public static PostResponses from(List<Post> posts) {
        return new PostResponses(posts.stream()
                .map(PostResponse::from)
                .toList());
    }
}
