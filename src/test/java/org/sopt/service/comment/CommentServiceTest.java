package org.sopt.service.comment;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.comment.CommentCreateRequest;
import org.sopt.dto.request.comment.CommentUpdateRequest;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.ForbiddenException;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.UserRepository;
import org.sopt.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class CommentServiceTest {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentService commentService;

	@AfterEach
	public void afterTest() {
		commentRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("유효한 유저와 게시글에 대한 댓글 생성이 정상적으로 완료됨")
	@Test
	void createComment() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		CommentCreateRequest request = new CommentCreateRequest("commentA");

		// when
		Comment commentA = commentService.createPostComment(postA.getId(), userA.getId(), request);

		// then
		Comment result = commentRepository.findById(commentA.getId()).get();

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEqualTo("commentA");
	}

	@DisplayName("작성자가 자신의 댓글 수정을 시도할 경우 정상적으로 수정됨")
	@Test
	void updateCommentSameUser() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = Post.createNew(user, "postA", "contentA");
		Post postA = postRepository.save(post);

		CommentCreateRequest request = new CommentCreateRequest("commentA");
		Comment commentA = commentService.createPostComment(postA.getId(), userA.getId(), request);

		String updatedContent = "updatedA";
		CommentUpdateRequest updateRequest = new CommentUpdateRequest(Optional.of(updatedContent));
		// when
		commentService.updatePostComment(commentA.getId(), postA.getId(), userA.getId(), updateRequest);

		// then
		Comment result = commentRepository.findById(commentA.getId()).get();
		assertThat(result.getContent()).isEqualTo(updatedContent);
	}

	@DisplayName("유저가 자신이 작성하지 않은 댓글을 수정하려는 경우 예외가 발생함")
	@Test
	void updateCommentOtherUser() {
		// given
		User userA = new User("userA", "email");
		User userB = new User("userB", "email");
		userRepository.saveAll(List.of(userA, userB));

		Post post = Post.createNew(userA, "postA", "contentA");
		Post postA = postRepository.save(post);

		CommentCreateRequest request = new CommentCreateRequest("commentA");
		Comment commentA = commentService.createPostComment(postA.getId(), userA.getId(), request);

		String updatedContent = "updatedA";
		CommentUpdateRequest updateRequest = new CommentUpdateRequest(Optional.of(updatedContent));
		// when & then
		assertThatThrownBy(
			() -> commentService.updatePostComment(commentA.getId(), postA.getId(), userB.getId(), updateRequest))
			.isInstanceOf(ForbiddenException.class)
			.hasFieldOrProperty("errorCode");

	}

	@DisplayName("댓글의 게시글 정보가 아닌 다른 게시글로 댓글 수정을 시도하는 경우 예외가 발생함")
	@Test
	void updateCommentOtherPost() {
		// given
		User userA = new User("userA", "email");
		userRepository.saveAll(List.of(userA));

		Post postA = Post.createNew(userA, "postA", "contentA");
		Post postB = Post.createNew(userA, "postA", "contentA");
		postRepository.saveAll(List.of(postA, postB));

		CommentCreateRequest request = new CommentCreateRequest("commentA");
		Comment commentA = commentService.createPostComment(postA.getId(), userA.getId(), request);

		String updatedContent = "updatedA";
		CommentUpdateRequest updateRequest = new CommentUpdateRequest(Optional.of(updatedContent));
		// when & then
		assertThatThrownBy(
			() -> commentService.updatePostComment(commentA.getId(), postB.getId(), userA.getId(), updateRequest))
			.isInstanceOf(BadRequestException.class)
			.hasFieldOrProperty("errorCode");
	}

	@DisplayName("유저가 자신이 작성한 댓글을 정상적으로 삭제할 수 있음")
	@Test
	void deleteComment() {
		// given
		User userA = new User("userA", "email");
		userRepository.saveAll(List.of(userA));

		Post postA = Post.createNew(userA, "postA", "contentA");
		postRepository.saveAll(List.of(postA));

		Comment commentA = new Comment(postA, userA, "commentA");
		commentRepository.save(commentA);

		// when & then
		assertThat(commentRepository.findAll()).hasSize(1);

		commentService.deletePostComment(commentA.getId(), postA.getId(), userA.getId());
		assertThat(commentRepository.findAll()).hasSize(0);
	}

}