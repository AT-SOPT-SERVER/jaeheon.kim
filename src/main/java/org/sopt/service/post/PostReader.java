package org.sopt.service.post;

import org.sopt.domain.Post;
import org.sopt.dto.response.PostResponse;
import org.sopt.dto.response.PostResponses;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.PostRepository;
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
        Post post = findById(id);
        return PostResponse.from(post);
    }

    public PostResponses getPosts(final String keyword) {
        if (keyword != null) {
            return PostResponses.from(postRepository.findAllByTitleContaining(keyword));
        }
        return PostResponses.from(postRepository.findAll());
    }

    public boolean isExistTitle(final String title) {
        return postRepository.existsByTitle(title);
    }
}
