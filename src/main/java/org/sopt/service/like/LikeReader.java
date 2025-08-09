package org.sopt.service.like;

import java.util.Optional;

import org.sopt.domain.Comment;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.domain.enums.ContentType;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.LikeRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class LikeReader {
	private final LikeRepository likeRepository;

	public LikeReader(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}

	public boolean isUserLikedPost(Post post, User user) {
		return likeRepository.exists(Example.of(Like.createLike(user, post)));
	}

	public boolean isUserLikedComment(Comment comment, User user) {
		return likeRepository.exists(Example.of(Like.createLike(user, comment)));
	}

	public Like findByPostAndUserForWrite(Post post, User user) {
		Optional<Like> like = likeRepository.findByUserIdAndContentIdAndContentTypeWithExclusiveLock(
			user.getId(),
			post.getId(),
			ContentType.fromType(post));

		return like.orElseThrow(() -> new NotFoundException(ErrorCode.POST_LIKE_NOT_FOUND));
	}

	public Like findByCommentAndUserForWrite(Comment comment, User user) {
		Optional<Like> like = likeRepository.findByUserIdAndContentIdAndContentTypeWithExclusiveLock(
			user.getId(),
			comment.getId(),
			ContentType.fromType(comment));

		return like.orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_LIKE_NOT_FOUND));
	}

}
