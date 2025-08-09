package org.sopt.service.comment;

import static org.sopt.exception.errorcode.ErrorCode.*;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.comment.CommentCreateRequest;
import org.sopt.dto.request.comment.CommentUpdateRequest;
import org.sopt.service.post.PostReader;
import org.sopt.service.post.PostWriter;
import org.sopt.service.user.UserReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
	private final CommentReader commentReader;
	private final CommentWriter commentWriter;
	private final PostReader postReader;
	private final PostWriter postWriter;
	private final UserReader userReader;

	public CommentService(
		CommentReader commentReader,
		CommentWriter commentWriter,
		PostReader postReader, PostWriter postWriter,
		UserReader userReader
	) {
		this.commentReader = commentReader;
		this.commentWriter = commentWriter;
		this.postReader = postReader;
		this.postWriter = postWriter;
		this.userReader = userReader;
	}

	@Transactional
	public Comment createPostComment(Long postId, Long userId, CommentCreateRequest request) {
		Post post = postReader.findById(postId);
		User user = userReader.findById(userId);

		return commentWriter.createComment(post, user, request.content());
	}

	@Transactional
	public Comment updatePostComment(Long commentId, Long postId, Long userId, CommentUpdateRequest request) {
		Comment comment = commentReader.findById(commentId);
		User user = userReader.findById(userId);

		comment.checkPostIdIntegrity(postId);

		user.checkIsWriter(comment.getUser(), NOT_ALLOWED_COMMENT);

		return commentWriter.update(comment, request);
	}

	@Transactional
	public void deletePostComment(Long commentId, Long postId, Long userId) {
		Comment comment = commentReader.findById(commentId);

		comment.checkPostIdIntegrity(postId);

		User user = userReader.findById(userId);
		user.checkIsWriter(comment.getUser(), NOT_ALLOWED_COMMENT);

		Post post = postReader.findById(postId);

		commentWriter.delete(comment, post);
	}

}
