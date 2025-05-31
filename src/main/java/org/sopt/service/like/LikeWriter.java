package org.sopt.service.like;

import org.sopt.domain.Comment;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeWriter {
	private final LikeRepository likeRepository;

	public LikeWriter(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}

	public Like addPostLike(Post post, User user) {
		Like like = Like.createLike(user, post);
		return likeRepository.save(like);
	}

	public Like addCommentLike(Comment comment, User user) {
		Like like = Like.createLike(user, comment);
		return likeRepository.save(like);
	}

	public void delete(Like like) {
		likeRepository.delete(like);
	}
}
