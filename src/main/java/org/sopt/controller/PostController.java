package org.sopt.controller;

import org.sopt.annotation.ValidPostRequest;
import org.sopt.annotation.ValidPostUpdateRequest;
import org.sopt.dto.ResponseDto;
import org.sopt.dto.request.post.PostRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.PostResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<?>> createPost(@RequestBody @ValidPostRequest final PostRequest postRequest) {
        postService.createPost(postRequest);
        return new ResponseEntity<>(ResponseDto.of(HttpStatus.CREATED, "post 생성 성공"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{post-id}")
    public Post getPostById(@PathVariable(name = "post-id") Long id) {
        return postService.getPost(id);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<?> deletePostById(@PathVariable(name = "post-id") Long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok("삭제");
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity<ResponseDto<?>> updatePostTitle(@PathVariable(name = "post-id") final Long id,
                                                          @RequestBody @ValidPostUpdateRequest PostUpdateRequest request) {
        postService.updateTitle(id, request);
        return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "post 수정 성공"), HttpStatus.OK);
    }

    @GetMapping
    public List<Post> searchPostsByKeyword(@RequestParam(name = "keyword") String keyword) {
        return postService.searchPostsByKeyword(keyword);
    }
}
