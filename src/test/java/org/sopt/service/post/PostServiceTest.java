package org.sopt.service.post;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.exception.ForbiddenException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.UserRepository;
import org.sopt.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PostServiceTest {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostService postService;

	@DisplayName("작성자가 게시글 수정을 시도할 경우 정상적으로 수정됨")
	@Test
	void updatePostSameUser() {
		// given
		User user = new User("userA", "email");
		User userA = userRepository.save(user);

		Post post = new Post(user, "postA", "contentA", null);
		Post postA = postRepository.save(post);

		String updatedContent = "updatedA";

		PostUpdateRequest request = new PostUpdateRequest(Optional.of(updatedContent), Optional.of(updatedContent));

		// when
		postService.updatePostById(postA.getId(), request, userA.getId());

		Post result = postRepository.findById(postA.getId()).get();
		assertThat(result.getTitle()).isEqualTo(updatedContent);
		assertThat(result.getContent()).isEqualTo(updatedContent);
	}

	@DisplayName("작성자 아닌 유저가 게시글 수정을 시도할 경우 예외가 발생함")
	@Test
	void updatePostOtherUser() {
		// given
		User user = new User("userA", "email");
		User user2 = new User("user2", "email2");
		userRepository.saveAll(List.of(user, user2));

		Post post = new Post(user, "postA", "contentA", null);
		Post postA = postRepository.save(post);

		String updatedContent = "updatedA";
		PostUpdateRequest request = new PostUpdateRequest(Optional.of(updatedContent), Optional.of(updatedContent));

		// when & then
		assertThatThrownBy(() -> postService.updatePostById(postA.getId(), request, user2.getId()))
			.isInstanceOf(ForbiddenException.class)
			.satisfies(e -> {
				ForbiddenException forbiddenException = (ForbiddenException)e;
				assertThat(forbiddenException.getErrorCode()).isEqualTo(ErrorCode.NOT_ALLOWED_POST);
			});
	}

}