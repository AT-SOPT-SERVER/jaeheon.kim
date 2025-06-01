package org.sopt.repository;

import org.sopt.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Modifying
	@Query("""
		update Comment c
		set c.likeCount = c.likeCount + 1
		where c = :requestComment
		""")
	void increaseCommentLike(@Param("requestComment") Comment comment);

	@Modifying
	@Query("""
		update Comment c
		set c.likeCount = c.likeCount - 1
		where c = :requestComment
		""")
	void decreaseCommentLike(@Param("requestComment") Comment comment);
}
