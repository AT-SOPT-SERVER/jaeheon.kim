package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.*;

public class PostRepository {

    public List<Post> postList = new ArrayList<>();

    public Map<Long, Post> postMap = new HashMap<>();

    public void save(Post post){
        postList.add(post);
    }

    public List<Post> findAll(){
        return this.postList;
    }

    public Post findPostById(Long id){
        return this.postList.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElseGet(null);
    }

    public boolean deletePostById(Long id){
        Optional<Post> post = this.postList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if(post.isEmpty()){
            return false;
        }
        this.postList.remove(post.get());
        return true;
    }
}
