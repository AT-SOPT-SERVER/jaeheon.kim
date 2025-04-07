package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostRepository {

    public List<Post> postList = new ArrayList<>();

    public void save(Post post){
        postList.add(post);
    }

    public List<Post> findAll(){
        return this.postList;
    }

    public Post findPostById(int id){
        return this.postList.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElseGet(null);
    }

    public boolean deletePostById(int id){
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
