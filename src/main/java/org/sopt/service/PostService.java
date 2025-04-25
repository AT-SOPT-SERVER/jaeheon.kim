package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.post.PostRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public boolean createPost(PostRequest postRequest) {
        if (postRepository.existsByTitle(postRequest.title())) {
            return false;
        }
        Post post = Post.from(postRequest);
        postRepository.save(post);
        return true;
    }

    public List<Post> getPosts(final String keyword) {
        if (keyword != null) {
            return postRepository.findAllByTitleContaining(keyword);
        }
        return postRepository.findAll();
    }

    public PostResponse getPost(final Long id) {
        Post post = findById(id);
        return PostResponse.from(post);
    }

    public void deletePostById(final Long id) {
        postRepository.deleteById(id);
    }

    public void updateTitle(final Long id, final PostUpdateRequest request) {
        Post post = findById(id);

        if (request.title().isPresent()) {
            updatePostTitle(post, request.title().get());
//            post.updateTitle(request.newTitle().get());
        }
    }

    @Transactional
    protected void updatePostTitle(Post post, String title) {
        post.updateTitle(title);
    }
}
