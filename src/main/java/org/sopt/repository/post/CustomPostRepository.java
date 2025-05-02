package org.sopt.repository.post;

import org.sopt.domain.Post;
import org.sopt.domain.enums.Tag;

import java.util.List;
import java.util.Optional;

public interface CustomPostRepository {
    List<Post> searchPosts(Optional<String> keyword, String target, Optional<Tag> tag);
}
