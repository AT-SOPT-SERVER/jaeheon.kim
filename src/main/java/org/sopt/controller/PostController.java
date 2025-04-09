package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.request.PostUpdateRequest;
import org.sopt.service.PostService;

import java.util.List;

public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    public boolean createPost(String title){
        PostRequest postRequest = new PostRequest(title);
        return postService.createPost(postRequest);
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

    public boolean updatePostTitle(Long id, String title){
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(id, title);
        return postService.updatePostTitle(postUpdateRequest);
    }

    public List<Post> searchPostsByKeyword(String keyword){
        return postService.searchPostsByKeyword(keyword);
    }
}
