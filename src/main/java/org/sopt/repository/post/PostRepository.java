package org.sopt.repository.post;

import java.util.Optional;

import org.sopt.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

	@Query("select p " +
		"from Post p " +
		"inner join fetch p.user " +
		"where p.id = :postId")
	Optional<Post> findByIdWithUser(@Param("postId") Long postId);

	@Modifying
	@Query("""
		update Post p
		set p.likeCount = p.likeCount + 1
		where p = :requestPost
		""")
	void increasePostLike(@Param("requestPost") Post post);

	@Modifying
	@Query("""
		update Post p
		set p.likeCount = p.likeCount - 1
		where p = :requestPost
		""")
	void decreasePostLike(@Param("requestPost") Post post);

}
