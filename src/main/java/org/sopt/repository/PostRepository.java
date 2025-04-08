package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

    public List<Post> postList = new ArrayList<>();
    private final AtomicLong autoIncrement = new AtomicLong(0);

    public void save(PostRequest postRequest){
        Long newId = autoIncrement.getAndIncrement();
        postList.add(new Post(newId, postRequest.getTitle()));
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
