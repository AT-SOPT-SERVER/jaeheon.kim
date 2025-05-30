package org.sopt.service.comment;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.comment.CommentCreateRequest;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.UserRepository;
import org.sopt.repository.post.PostRepository;
import org.sopt.service.post.PostReader;
import org.sopt.service.post.PostWriter;
import org.sopt.service.user.UserReader;
import org.sopt.service.user.UserWriter;
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
	private CommentWriter commentWriter;
	@Autowired
	private PostReader postReader;
	@Autowired
	private PostWriter postWriter;
	@Autowired
	private UserReader userReader;
	@Autowired
	private UserWriter userWriter;
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

		Post post = new Post(user, "postA", "contentA", null);
		Post postA = postRepository.save(post);

		CommentCreateRequest request = new CommentCreateRequest("commentA");

		// when
		Comment commentA = commentService.createPostComment(postA.getId(), userA.getId(), request);

		// then
		Comment result = commentRepository.findById(commentA.getId()).get();

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEqualTo("commentA");
	}

}