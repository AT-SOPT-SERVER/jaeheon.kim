package org.sopt.service.like;

import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.exception.ConflictException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.service.post.PostReader;
import org.sopt.service.user.UserReader;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
	private final LikeReader likeReader;
	private final LikeWriter likeWriter;
	private final PostReader postReader;
	private final UserReader userReader;

	public LikeService(LikeReader likeReader, LikeWriter likeWriter, PostReader postReader, UserReader userReader) {
		this.likeReader = likeReader;
		this.likeWriter = likeWriter;
		this.postReader = postReader;
		this.userReader = userReader;
	}

	public Like addPostLike(Long postId, Long userId) {
		Post post = postReader.findById(postId);
		User user = userReader.findById(userId);

		if (likeReader.isUserLikedPost(post, user)) {
			throw new ConflictException(ErrorCode.POST_ALREADY_LIKED);
		}

		return likeWriter.addPostLike(post, user);
	}

}
