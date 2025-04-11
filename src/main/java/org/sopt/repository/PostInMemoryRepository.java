package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.util.IdGenerator;
import org.sopt.util.LongIdGenerator;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class PostInMemoryRepository implements PostRepository{

    private final Map<Long, Post> postMap = new HashMap<>();
    private final IdGenerator<Long> idGenerator = new LongIdGenerator(0L);

    public synchronized void save(Post post){
        Long newId = idGenerator.generateId();
        post.setId(newId);
        postMap.put(newId, new Post(newId, post.getTitle()));
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

    public synchronized boolean updatePostTitle(Long id, String newTitle){
        Post post = findPostById(id);
        if (post == null){
            return false;
        }
        postMap.put(id, post.updateTitle(newTitle));
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
