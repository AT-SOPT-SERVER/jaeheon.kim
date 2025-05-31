package org.sopt.service.comment;

import org.sopt.domain.Comment;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentReader {
	private final CommentRepository commentRepository;

	public CommentReader(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public Comment findById(Long id) {
		return commentRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
	}

}
