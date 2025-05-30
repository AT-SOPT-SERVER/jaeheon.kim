package org.sopt.service.comment;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.comment.CommentCreateRequest;
import org.sopt.service.post.PostReader;
import org.sopt.service.user.UserReader;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	private final CommentWriter commentWriter;
	private final PostReader postReader;
	private final UserReader userReader;

	public CommentService(final CommentWriter commentWriter, PostReader postReader, UserReader userReader) {
		this.commentWriter = commentWriter;
		this.postReader = postReader;
		this.userReader = userReader;
	}

	public Comment createPostComment(Long postId, Long userId, CommentCreateRequest request) {
		Post post = postReader.findById(postId);
		User user = userReader.findById(userId);

		return commentWriter.createComment(post, user, request.content());
	}
}
