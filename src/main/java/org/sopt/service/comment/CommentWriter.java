package org.sopt.service.comment;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.comment.CommentUpdateRequest;
import org.sopt.repository.CommentRepository;
import org.sopt.service.post.PostWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentWriter {
	private final CommentRepository commentRepository;
	private final PostWriter postWriter;

	public CommentWriter(final CommentRepository commentRepository, PostWriter postWriter) {
		this.commentRepository = commentRepository;
		this.postWriter = postWriter;
	}

	@Transactional
	public Comment createComment(Post post, User user, String content) {
		Comment comment = new Comment(post, user, content);
		postWriter.increaseCommentCount(post); // ㅇ
		return commentRepository.save(comment);
	}

	@Transactional
	public Comment update(Comment comment, CommentUpdateRequest request) {
		if (request.content().isPresent()) {
			comment.updateContent(request.content().get());
		}
		return comment;
	}

	@Transactional
	public void delete(Comment comment, Post post) {
		postWriter.decreaseCommentCount(post);
		commentRepository.delete(comment);
	}

	public void increaseCommentLike(Comment comment) {
		commentRepository.increaseCommentLike(comment);
	}

	public void decreaseCommentLike(Comment comment) {
		commentRepository.decreaseCommentLike(comment);
	}
}
