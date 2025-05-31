package org.sopt.service.like;

import static org.assertj.core.api.Assertions.*;
import static org.sopt.domain.enums.ContentType.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.exception.ConflictException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.UserRepository;
import org.sopt.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class LikeServiceTest {

	@Autowired
	private LikeService likeService;

	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	@AfterEach
	public void afterTest() {
		likeRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("좋아요를 누르지 않은 게시글에 좋아요를 성공적으로 생성함")
	@Test
	void createPostLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = new Post(user, "postA", "contentA", null);
		Post postA = postRepository.save(post);

		// when
		likeService.addPostLike(postA.getId(), userA.getId());

		// then
		List<Like> result = likeRepository.findAll();

		assertThat(result).hasSize(1)
			.extracting("id.userId", "id.contentId", "id.contentType")
			.contains(
				tuple(userA.getId(), postA.getId(), POST)
			);

		// assertThat(result.get(0).getId())
		// 	.extracting("userId", "contentId", "contentType")
		// 	.contains(
		// 		tuple(userA.getId(), postA.getId(), POST)
		// 	);
	}

	@DisplayName("이미 좋아요를 누른 게시글에 다시 좋아요 생성을 요청할 경우 예외가 발생함")
	@Test
	void createDuplicatedPostLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = new Post(user, "postA", "contentA", null);
		Post postA = postRepository.save(post);

		likeService.addPostLike(postA.getId(), userA.getId());
		// when & then
		assertThatThrownBy(() -> likeService.addPostLike(postA.getId(), userA.getId()))
			.isInstanceOf(ConflictException.class)
			.satisfies(e -> {
				ConflictException conflictException = (ConflictException)e;
				assertThat(conflictException.getErrorCode()).isEqualTo(ErrorCode.POST_ALREADY_LIKED);
			});
	}

}