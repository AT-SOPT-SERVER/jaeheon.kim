package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.request.PostUpdateRequest;
import org.sopt.repository.PostRepository;
import org.sopt.validator.PostValidator;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public boolean createPost(PostRequest postRequest){
        if (!PostValidator.isValidTitle(postRequest.getTitle())){
            return false;
        }

        postRepository.save(postRequest);
        return true;
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post getPost(Long id){
        return postRepository.findPostById(id);
    }

    public boolean deletePostById(Long id){
        return postRepository.deletePostById(id);
    }

    public boolean updatePostTitle(PostUpdateRequest request){
        if (!PostValidator.isValidTitle(request.getTitle())){
            return false;
        }
        return postRepository.updatePostTitle(request);
    }
}
