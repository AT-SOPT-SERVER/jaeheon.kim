package org.sopt.repository.post;

import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PostJpaRepository, CustomPostRepository {
}
