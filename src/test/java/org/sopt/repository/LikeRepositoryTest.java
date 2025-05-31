package org.sopt.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.domain.enums.ContentType;
import org.sopt.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class LikeRepositoryTest {

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

	@DisplayName("userId, contentId, contentType을 사용하여 Like를 정상적으로 찾을 수 있음")
	@Transactional // 비관적, 배타락을 사용하므로
	@Test
	void findByUserIdAndContentIdAndContentTypeWithExclusiveLock() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = new Post(user, "postA", "contentA", null);
		Post postA = postRepository.save(post);

		Like likeA = likeRepository.save(Like.createLike(userA, postA));
		assertThat(likeRepository.findAll()).hasSize(1);

		// when

		Optional<Like> like = likeRepository.findByUserIdAndContentIdAndContentTypeWithExclusiveLock(userA.getId(),
			postA.getId(),
			ContentType.POST);

		// then
		assertThat(like.get()).isEqualTo(likeA);
	}
}