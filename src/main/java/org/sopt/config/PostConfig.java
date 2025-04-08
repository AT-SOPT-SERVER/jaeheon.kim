package org.sopt.config;

import org.sopt.controller.PostController;
import org.sopt.repository.PostFileRepository;
import org.sopt.repository.PostInMemoryRepository;
import org.sopt.repository.PostRepository;
import org.sopt.service.PostService;

public class PostConfig {

    public static PostController configPostController(){
        PostRepository postRepository = postFileRepository();
        PostService postService = new PostService(postRepository);
        PostController controller = new PostController(postService);
        return controller;
    }

    private static PostRepository postInMemoryRepository(){
        return new PostInMemoryRepository();
    }

    private static PostRepository postFileRepository(){
        return new PostFileRepository();
    }
}
