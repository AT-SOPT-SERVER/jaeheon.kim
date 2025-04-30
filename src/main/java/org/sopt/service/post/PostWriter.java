package org.sopt.service.post;

import org.sopt.domain.Post;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostWriter {
    private final PostRepository postRepository;

    public PostWriter(final PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void create(final Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void updateTitle(final Post post, final PostUpdateRequest request) {
        if (request.title().isPresent()) {
            post.updateTitle(request.title().get());
        }
        if (request.content().isPresent()) {
            post.updateContent(request.content().get());
        }
    }

    public void delete(final Post post) {
        postRepository.delete(post);
    }

}
