package org.sopt.service.comment;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.comment.CommentCreateRequest;
import org.sopt.dto.request.comment.CommentUpdateRequest;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.ForbiddenException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.service.post.PostReader;
import org.sopt.service.user.UserReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
	private final CommentReader commentReader;
	private final CommentWriter commentWriter;
	private final PostReader postReader;
	private final UserReader userReader;

	public CommentService(
		CommentReader commentReader,
		CommentWriter commentWriter,
		PostReader postReader,
		UserReader userReader
	) {
		this.commentReader = commentReader;
		this.commentWriter = commentWriter;
		this.postReader = postReader;
		this.userReader = userReader;
	}

	public Comment createPostComment(Long postId, Long userId, CommentCreateRequest request) {
		Post post = postReader.findById(postId);
		User user = userReader.findById(userId);

		return commentWriter.createComment(post, user, request.content());
	}

	@Transactional
	public Comment updatePostComment(Long commentId, Long postId, Long userId, CommentUpdateRequest request) {
		Comment comment = commentReader.findById(commentId);
		User user = userReader.findById(userId);

		if (!postId.equals(comment.getPost().getId())) {
			throw new BadRequestException(ErrorCode.INVALID_POST_ID);
		}

		checkWriterIsUser(comment.getUser(), user);

		return commentWriter.update(comment, request);
	}

	private void checkWriterIsUser(User writer, User user) {
		if (!user.equals(writer)) {
			throw new ForbiddenException(ErrorCode.NOT_ALLOWED_COMMENT);
		}
	}
}
