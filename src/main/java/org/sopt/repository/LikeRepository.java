package org.sopt.repository;

import org.sopt.domain.Like;
import org.sopt.domain.id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
}
