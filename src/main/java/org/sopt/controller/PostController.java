package org.sopt.controller;

import org.sopt.annotation.ValidPostRequest;
import org.sopt.annotation.ValidPostUpdateRequest;
import org.sopt.dto.ResponseDto;
import org.sopt.dto.request.post.PostRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.PostResponse;
import org.sopt.dto.response.PostResponses;
import org.sopt.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createPost(@RequestBody @ValidPostRequest final PostRequest postRequest) {
        postService.createPost(postRequest);
        return new ResponseEntity<>(ResponseDto.of(HttpStatus.CREATED, "post 생성 성공"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PostResponses>> getPosts(@RequestParam(name = "keyword", required = false) final String keyword) {

        return new ResponseEntity<>(
                ResponseDto.of(HttpStatus.OK, "post 목록 조회 성공", postService.getPosts(keyword)), HttpStatus.OK);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<ResponseDto<PostResponse>> getPostById(@PathVariable(name = "post-id") final Long id) {
        PostResponse postResponse = postService.getPostById(id);
        return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "post 단일 조회 성공", postResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<ResponseDto<Void>> deletePostById(@PathVariable(name = "post-id") final Long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "post 삭제 성공"), HttpStatus.OK);
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity<ResponseDto<Void>> updatePostTitle(@PathVariable(name = "post-id") final Long id,
                                                             @RequestBody @ValidPostUpdateRequest PostUpdateRequest request) {
        postService.updatePostById(id, request);
        return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "post 수정 성공"), HttpStatus.OK);
    }

}
