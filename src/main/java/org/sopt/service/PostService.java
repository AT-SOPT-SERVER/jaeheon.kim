package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;
import org.sopt.repository.PostRepository;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public void createPost(PostRequest postRequest){
        postRepository.save(postRequest);
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
}
