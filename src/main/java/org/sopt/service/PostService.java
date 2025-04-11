package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;
import org.sopt.repository.PostRepository;
import org.sopt.validator.PostValidator;

import java.util.List;

public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public boolean createPost(PostRequest postRequest){
        if (!PostValidator.isValidTitle(postRequest.getTitle())
                || postRepository.isDuplicatedTitle(postRequest.getTitle())){
            return false;
        }
        Post post = new Post(postRequest);
        postRepository.save(post);
        return true;
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post getPost(Long id){
        return postRepository.findPostById(id);
    }

    public boolean deletePostById(Long id){
        return postRepository.deletePostById(id);
    }

    public boolean updatePostTitle(Long id, String title){
        if (!PostValidator.isValidTitle(title)
                || postRepository.isDuplicatedTitle(title)){
            return false;
        }
        return postRepository.updatePostTitle(id, title);
    }

    public List<Post> searchPostsByKeyword(String keyword){
        return postRepository.findAllByKeyword(keyword);
    }
}
