package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;
import org.sopt.service.PostService;

import java.util.List;

public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    public void createPost(String title){
        PostRequest postRequest = new PostRequest(title);
        postService.createPost(postRequest);
    }

    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    public Post getPostById(Long id){
        return postService.getPost(id);
    }

    public boolean deletePostById(Long id){
        return postService.deletePostById(id);
    }
}
