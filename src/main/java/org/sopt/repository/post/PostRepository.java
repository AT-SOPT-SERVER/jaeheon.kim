package org.sopt.repository.post;

import org.sopt.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @Query("select p " +
            "from Post p " +
            "inner join fetch p.user " +
            "where p.id = :postId")
    Optional<Post> findByIdWithUser(@Param("postId") Long postId);

}
