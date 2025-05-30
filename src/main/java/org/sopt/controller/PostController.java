package org.sopt.controller;

import static org.sopt.constant.PostConstant.*;

import java.util.Optional;

import org.sopt.annotation.ValidPostCreateRequest;
import org.sopt.annotation.ValidPostUpdateRequest;
import org.sopt.dto.ResponseDto;
import org.sopt.dto.request.comment.CommentCreateRequest;
import org.sopt.dto.request.post.PostCreateRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.post.PostPreviewResponses;
import org.sopt.dto.response.post.PostResponse;
import org.sopt.service.comment.CommentService;
import org.sopt.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;
	private final CommentService commentService;

	public PostController(final PostService postService, CommentService commentService) {
		this.postService = postService;
		this.commentService = commentService;
	}

	@PostMapping
	public ResponseEntity<ResponseDto<Void>> createPost(
		@RequestBody @ValidPostCreateRequest final PostCreateRequest postCreateRequest,
		@RequestHeader Long userId) {
		postService.createPost(userId, postCreateRequest);
		return new ResponseEntity<>(ResponseDto.of(HttpStatus.CREATED, "post 생성 성공"), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<ResponseDto<PostPreviewResponses>> getPosts(
		@RequestParam(name = "keyword", required = false) final Optional<String> keyword,
		@RequestParam(name = "target", defaultValue = DEFAULT_SEARCH_TARGET) final String target,
		@RequestParam(name = "tag", required = false) final Optional<String> tag
	) {
		PostPreviewResponses responses = postService.getPosts(keyword, target, tag);
		return new ResponseEntity<>(
			ResponseDto.of(HttpStatus.OK, "post 목록 조회 성공", responses), HttpStatus.OK);
	}

	@GetMapping("/{post-id}")
	public ResponseEntity<ResponseDto<PostResponse>> getPostById(@PathVariable(name = "post-id") final Long id) {
		PostResponse postResponse = postService.getPostById(id);
		return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "post 단일 조회 성공", postResponse), HttpStatus.OK);
	}

	@DeleteMapping("/{post-id}")
	public ResponseEntity<ResponseDto<Void>> deletePostById(
		@PathVariable(name = "post-id") final Long postId,
		@RequestHeader Long userId) {
		postService.deletePostById(postId, userId);
		return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "post 삭제 성공"), HttpStatus.OK);
	}

	@PatchMapping("/{post-id}")
	public ResponseEntity<ResponseDto<Void>> updatePostTitle(
		@PathVariable(name = "post-id") final Long postId,
		@RequestBody @ValidPostUpdateRequest PostUpdateRequest request,
		@RequestHeader Long userId) {
		postService.updatePostById(postId, request, userId);
		return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "post 수정 성공"), HttpStatus.OK);
	}

	@PostMapping("/{post-id}/comment")
	public ResponseEntity<ResponseDto<Void>> creatComment(
		@PathVariable(name = "post-id") Long postId,
		@RequestBody CommentCreateRequest request,
		@RequestHeader Long userId
	) {
		commentService.createPostComment(postId, userId, request);
		return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "게시글 댓글 작성 성공"), HttpStatus.OK);
	}

}
