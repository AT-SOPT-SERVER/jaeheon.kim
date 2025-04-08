package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.request.PostUpdateRequest;

import java.util.List;

public interface PostRepository {
    public void save(PostRequest postRequest);
    public List<Post> findAll();
    public Post findPostById(Long id);
    public boolean deletePostById(Long id);
    public boolean updatePostTitle(PostUpdateRequest postUpdateRequest);
}
