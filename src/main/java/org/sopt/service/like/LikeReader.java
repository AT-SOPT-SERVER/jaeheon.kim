package org.sopt.service.like;

import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
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
}
