package org.sopt.service.like;

import static org.assertj.core.api.Assertions.*;
import static org.sopt.domain.enums.ContentType.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.Comment;
import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.exception.ConflictException;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.CommentRepository;
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
	@Autowired
	private CommentRepository commentRepository;

	@AfterEach
	public void afterTest() {
		likeRepository.deleteAllInBatch();
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("좋아요를 누르지 않은 게시글에 좋아요를 성공적으로 생성함")
	@Test
	void createPostLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
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

		assertThat(postRepository.findById(postA.getId()).get().getLikeCount())
			.isEqualTo(1);

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

		Post post = Post.createNew(user, "postA", "contentA");
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

	@DisplayName("좋아요가 존재하는 게시글에 대해 좋아요를 성공적으로 삭제함")
	@Test
	void deletePostLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		Like likeA = likeRepository.save(Like.createLike(userA, postA));
		assertThat(likeRepository.findAll()).hasSize(1);

		// when
		likeService.deletePostLike(postA.getId(), userA.getId());

		// then
		List<Like> result = likeRepository.findAll();

		assertThat(result).hasSize(0);
	}

	@DisplayName("좋아요가 존재하지 않는 게시글에 좋아요 삭제 요청을 할 경우 예외가 발생함")
	@Test
	void deleteNotExistPostLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		// when & then
		assertThatThrownBy(() -> likeService.deletePostLike(postA.getId(), userA.getId()))
			.isInstanceOf(NotFoundException.class)
			.satisfies(e -> {
				NotFoundException notFoundException = (NotFoundException)e;
				assertThat(notFoundException.getErrorCode()).isEqualTo(ErrorCode.POST_LIKE_NOT_FOUND);
			});
	}

	@DisplayName("좋아요를 누르지 않은 댓글에 좋아요를 성공적으로 생성함")
	@Test
	void createCommentLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		Comment comment = new Comment(postA, userA, "commentA");
		Comment commentA = commentRepository.save(comment);

		// when
		likeService.addCommentLike(commentA.getId(), postA.getId(), userA.getId());

		// then
		assertThat(likeRepository.findAll()).hasSize(1)
			.extracting("id.userId", "id.contentId", "id.contentType")
			.contains(
				tuple(userA.getId(), commentA.getId(), COMMENT)
			);
	}

	@DisplayName("이미 유저의 좋아요가 존재하는 댓글에 좋아요 생성 요청을 시도할 경우 예외가 발생함")
	@Test
	void createDuplicatedCommentLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		Comment comment = new Comment(postA, userA, "commentA");
		Comment commentA = commentRepository.save(comment);

		likeRepository.save(Like.createLike(user, commentA));

		assertThat(likeRepository.findAll()).hasSize(1);
		// when & then
		assertThatThrownBy(() -> likeService.addCommentLike(commentA.getId(), postA.getId(), userA.getId()))
			.isInstanceOf(ConflictException.class)
			.satisfies(e -> {
				ConflictException conflictException = (ConflictException)e;
				assertThat(conflictException.getErrorCode()).isEqualTo(ErrorCode.COMMENT_ALREADY_LIKED);
			});
	}

	@DisplayName("좋아요가 존재하는 댓글에 대해 좋아요를 성공적으로 삭제함")
	@Test
	void deleteCommentLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		Comment comment = new Comment(postA, userA, "commentA");
		Comment commentA = commentRepository.save(comment);

		Like likeA = likeRepository.save(Like.createLike(userA, commentA));
		assertThat(likeRepository.findAll()).hasSize(1);

		// when
		likeService.deleteCommentLike(commentA.getId(), postA.getId(), userA.getId());

		// then
		List<Like> result = likeRepository.findAll();
		assertThat(result).hasSize(0);
	}

	@DisplayName("좋아요가 존재하지 않는 댓글에 좋아요 삭제 요청을 할 경우 예외가 발생함")
	@Test
	void deleteNotExistCommentLike() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		Comment comment = new Comment(postA, userA, "commentA");
		Comment commentA = commentRepository.save(comment);

		// when & then
		assertThatThrownBy(() -> likeService.deleteCommentLike(commentA.getId(), postA.getId(), userA.getId()))
			.isInstanceOf(NotFoundException.class)
			.satisfies(e -> {
				NotFoundException notFoundException = (NotFoundException)e;
				assertThat(notFoundException.getErrorCode()).isEqualTo(ErrorCode.COMMENT_LIKE_NOT_FOUND);
			});
	}

	/**
	 * 최초 addPostLike의 동시성을 고려하지 않았을 경우 좋아요 수는 100개의 요청에 대해 10~11개만 증가하여 테스트를 실패했었음.
	 * 고려한 방법은 아래와 같은데
	 * 낙관적 락 -> 실패가 자주 발생하고 이걸 폴링으로 재시도 처리할 경우 자원 낭비가 심함.
	 * 비관적 락 -> likeCount 하나 증가를 위해서 해당 레코드의 락을 잡는 것도 너무 비용이 많이 듦
	 * redis 같은 걸로 모아서 처리 -> 솔직히 좋아보여서 추후 이 방법을 사용하고 싶지만, 현재는 redis를 사용하지 않을 예정이므로 패스
	 * 증감 쿼리 -> 현재 채택한 방법, 기본적으로 RDB 는 CA 이므로 대부분 원자성을 보장하지만,,, DB 마다 다를 수 있으므로 조심해야 함.
	 */
	@DisplayName("유저 100명이 동시에 좋아요를 누르는 경우 레이스 컨디션이 발생하지 않고 정상적으로 likeCount 가 100이 됨")
	@Test
	void concurrentPostLike() throws InterruptedException {
		// given

		int requestCount = 100;

		ExecutorService executorService = Executors.newFixedThreadPool(requestCount);

		CountDownLatch countDownLatch = new CountDownLatch(requestCount);

		List<User> users = new LinkedList<>();

		for (int i = 0; i < requestCount; i++) {
			users.add(new User("user" + i, "email" + i));
		}

		userRepository.saveAll(users);

		Post post = Post.createNew(users.get(0), "postA", "contentA");
		Post postA = postRepository.save(post);

		// when
		for (User user : users) {
			executorService.submit(() -> {
				try {
					likeService.addPostLike(postA.getId(), user.getId());
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		executorService.shutdown();

		// then
		assertThat(likeRepository.findAll()).hasSize(requestCount);
		assertThat(postRepository.findById(postA.getId()).get().getLikeCount())
			.isEqualTo(requestCount);
	}

}