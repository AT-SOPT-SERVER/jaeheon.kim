package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.service.PostService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PostController {

    private final AtomicInteger autoIncrement = new AtomicInteger(0);
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    public void createPost(String title){
        //혹은 synchronized 블럭이나 메서드
        int newId = autoIncrement.getAndIncrement();
        postService.createPost(new Post(newId, title));
    }

    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    public Post getPostById(int id){
        return postService.getPost(id);
    }

    public boolean deletePostById(int id){
        return postService.deletePostById(id);
    }
}
