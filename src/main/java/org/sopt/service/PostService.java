package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.post.PostRequest;
import org.sopt.repository.PostRepository;
import org.sopt.validator.PostValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean createPost(PostRequest postRequest) {
        if (!PostValidator.isValidTitle(postRequest.title())
                || postRepository.existsByTitle(postRequest.title())) {
            return false;
        }
        Post post = Post.from(postRequest);
        postRepository.save(post);
        return true;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElse(null);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    public boolean updateTitle(Long id, String title) {
        Post post = this.getPost(id);
        if (!PostValidator.isValidTitle(title)
                || post == null
                || postRepository.existsByTitle(title)) {
            return false;
        }
        updatePostTitle(post, title); // self-invocation 해결을 위해 파서드 패턴 적용을 고민 중
        return true;
    }


    public List<Post> searchPostsByKeyword(String keyword) {
        return postRepository.findAllByTitleContaining(keyword);
    }

    @Transactional
    protected void updatePostTitle(Post post, String title) {
        post.updateTitle(title);
    }
}
