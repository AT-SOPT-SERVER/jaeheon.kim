package org.sopt.repository;

import java.util.Optional;

import org.sopt.domain.Like;
import org.sopt.domain.enums.ContentType;
import org.sopt.domain.id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = """
		select l
		from Like l
		where l.id.userId = :userId
			and l.id.contentId = :contentId
			and l.id.contentType = :contentType
		""")
	Optional<Like> findByUserIdAndContentIdAndContentTypeWithExclusiveLock(
		@Param("userId") Long userId,
		@Param("contentId") Long contentId,
		@Param("contentType") ContentType contentType);
}
