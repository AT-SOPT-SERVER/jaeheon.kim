package org.sopt.service.post;

import org.sopt.domain.Post;
import org.sopt.dto.request.post.PostRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.PostResponse;
import org.sopt.dto.response.PostResponses;
import org.sopt.exception.ConflictException;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(final PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post findById(final Long id) {
        return postRepository.findById(id).orElseThrow(()
                -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    public void createPost(final PostRequest postRequest) {
        if (isExistTitle(postRequest.title())) {
            throw new ConflictException(ErrorCode.POST_TITLE_CONFLICT);
        }
        Post post = new Post(postRequest.title());
        postRepository.save(post);
    }

    public PostResponses getPosts(final String keyword) {
        if (keyword != null) {
            return PostResponses.from(postRepository.findAllByTitleContaining(keyword));
        }
        return PostResponses.from(postRepository.findAll());
    }

    public PostResponse getPost(final Long id) {
        Post post = findById(id);
        return PostResponse.from(post);
    }

    public void deletePostById(final Long id) {
        Post post = findById(id);
        postRepository.delete(post);
    }

    public void updateTitle(final Long id, final PostUpdateRequest request) {
        Post post = findById(id);

        if (request.title().isPresent()) {
            updatePostTitle(post, request.title().get());
        }
    }

    @Transactional
    protected void updatePostTitle(final Post post, final String title) {
        post.updateTitle(title);
    }

    private boolean isExistTitle(final String title) {
        return postRepository.existsByTitle(title);
    }
}
