package org.sopt.service.post;

import org.sopt.domain.Post;
import org.sopt.domain.enums.Tag;
import org.sopt.dto.response.post.PostPreviewResponses;
import org.sopt.dto.response.post.PostResponse;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.post.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Post post = findById(id);
        return PostResponse.from(post);
    }

    public PostPreviewResponses getPosts(final Optional<String> keyword,
                                         final String target,
                                         final Optional<Tag> tag) {
        List<Post> posts = postRepository.searchPosts(keyword, target, tag);

        return PostPreviewResponses.from(posts);
    }
}
