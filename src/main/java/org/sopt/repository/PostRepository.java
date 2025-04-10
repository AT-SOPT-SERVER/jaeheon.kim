package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.List;

public interface PostRepository {
    public void save(Post post);
    public List<Post> findAll();
    public Post findPostById(Long id);
    public boolean deletePostById(Long id);
    public boolean updatePostTitle(Long id, String title);
    public List<Post> findAllByKeyword(String keyword);
    public boolean isDuplicatedTitle(String title);
}
