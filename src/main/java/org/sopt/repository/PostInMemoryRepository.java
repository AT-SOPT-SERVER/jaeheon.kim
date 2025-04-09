package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.request.PostUpdateRequest;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class PostInMemoryRepository implements PostRepository{

    public Map<Long, Post> postMap = new HashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong(0);

    public synchronized void save(PostRequest postRequest){
        Long newId = autoIncrement.getAndIncrement();
        postMap.put(newId, new Post(newId, postRequest.getTitle()));
    }

    public List<Post> findAll(){
        return postMap.values()
                .stream()
                .toList();
    }

    public Post findPostById(Long id){
        return this.postMap.get(id);
    }

    public synchronized boolean deletePostById(Long id){
        Post post = postMap.remove(id);
        if(post == null){
            return false;
        }
        return true;
    }

    public synchronized boolean updatePostTitle(PostUpdateRequest postUpdateRequest){
        Long id = postUpdateRequest.getId();
        Post post = findPostById(id);
        if (post == null){
            return false;
        }
        postMap.put(id, post.updateTitle(postUpdateRequest.getTitle()));
        return true;
    }

    @Override
    public List<Post> findAllByKeyword(String keyword) {
        return postMap.values().stream()
                .filter(post -> post.getTitle().contains(keyword))
                .toList();
    }

    @Override
    public boolean isDuplicatedTitle(String title) {
        return postMap.values().stream()
                .map(Post::getTitle)
                .anyMatch(title::equals);
    }
}
