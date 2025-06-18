package org.sopt.service.like;

import java.util.function.Supplier;

import org.sopt.domain.Comment;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.exception.ConflictException;
import org.sopt.exception.CustomException;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.service.comment.CommentReader;
import org.sopt.service.comment.CommentWriter;
import org.sopt.service.post.PostReader;
import org.sopt.service.post.PostWriter;
import org.sopt.service.user.UserReader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
	private final LikeReader likeReader;
	private final LikeWriter likeWriter;
	private final PostReader postReader;
	private final PostWriter postWriter;
	private final UserReader userReader;
	private final CommentReader commentReader;
	private final CommentWriter commentWriter;

	public LikeService(LikeReader likeReader, LikeWriter likeWriter, PostReader postReader, PostWriter postWriter,
		UserReader userReader,
		CommentReader commentReader, CommentWriter commentWriter) {
		this.likeReader = likeReader;
		this.likeWriter = likeWriter;
		this.postReader = postReader;
		this.postWriter = postWriter;
		this.userReader = userReader;
		this.commentReader = commentReader;
		this.commentWriter = commentWriter;
	}

	@Transactional
	public Like addPostLike(Long postId, Long userId) {
		Post post = postReader.findById(postId);
		User user = userReader.findById(userId);

		if (likeReader.isUserLikedPost(post, user)) {
			throw new ConflictException(ErrorCode.POST_ALREADY_LIKED);
		}

		postWriter.increaseLikeCount(post);

		return optimisticLockForWrite(() -> likeWriter.addPostLike(post, user),
			new ConflictException(ErrorCode.POST_ALREADY_LIKED));
	}

	@Transactional
	public void deletePostLike(Long postId, Long userId) {
		Post post = postReader.findById(postId);
		User user = userReader.findById(userId);

		Like like = likeReader.findByPostAndUserForWrite(post, user);

		postWriter.decreaseLikeCount(post);

		optimisticLockForWrite(() -> likeWriter.delete(like),
			new NotFoundException(ErrorCode.POST_LIKE_NOT_FOUND));
	}

	@Transactional
	public Like addCommentLike(Long commentId, Long postId, Long userId) {
		Comment comment = commentReader.findById(commentId);
		comment.checkPostIdIntegrity(postId);

		User user = userReader.findById(userId);
		if (likeReader.isUserLikedComment(comment, user)) {
			throw new ConflictException(ErrorCode.COMMENT_ALREADY_LIKED);
		}

		commentWriter.increaseCommentLike(comment);

		return optimisticLockForWrite(() -> likeWriter.addCommentLike(comment, user),
			new ConflictException(ErrorCode.COMMENT_ALREADY_LIKED));
	}

	@Transactional
	public void deleteCommentLike(Long commentId, Long postId, Long userId) {
		Comment comment = commentReader.findById(commentId);
		comment.checkPostIdIntegrity(postId);

		User user = userReader.findById(userId);

		Like like = likeReader.findByCommentAndUserForWrite(comment, user);
		likeWriter.delete(like);

		commentWriter.decreaseCommentLike(comment);

		optimisticLockForWrite(() -> likeWriter.delete(like),
			new NotFoundException(ErrorCode.COMMENT_LIKE_NOT_FOUND));
	}

	private <T> T optimisticLockForWrite(Supplier<T> supplier, CustomException customException) {
		try {
			return supplier.get();
		} catch (DataIntegrityViolationException e) {
			throw customException;
		}
	}

	private void optimisticLockForWrite(Runnable runnable, CustomException customException) {
		try {
			runnable.run();
		} catch (DataIntegrityViolationException e) {
			throw customException;
		}
	}

}
