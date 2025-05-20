package org.sopt.dto.response.post;

import org.sopt.domain.Post;
import org.sopt.domain.User;

import java.util.List;

public record PostPreviewResponses(List<PostPreviewResponse> responseList) {
    public static PostPreviewResponses from(List<Post> posts) {
        return new PostPreviewResponses(posts.stream()
                .map(PostPreviewResponse::from)
                .toList());
    }

    public record PostPreviewResponse(Long postId,
                                      String title,
                                      Long writerId,
                                      String writerName
    ) {
        public static PostPreviewResponse from(Post post) {
            User user = post.getUser();
            return new PostPreviewResponse(post.getId(), post.getTitle(), user.getId(), user.getName());
        }

    }
}
