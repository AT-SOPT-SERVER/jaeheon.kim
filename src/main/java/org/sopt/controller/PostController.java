package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.service.PostService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PostController {

    private final AtomicLong autoIncrement = new AtomicLong(0);
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    public void createPost(String title){
        //혹은 synchronized 블럭이나 메서드
        Long newId = autoIncrement.getAndIncrement();
        postService.createPost(new Post(newId, title));
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
