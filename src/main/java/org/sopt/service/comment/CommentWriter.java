package org.sopt.service.comment;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentWriter {
	private final CommentRepository commentRepository;

	public CommentWriter(final CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public Comment createComment(Post post, User user, String content) {
		Comment comment = new Comment(post, user, content);
		return commentRepository.save(comment);
	}
}
