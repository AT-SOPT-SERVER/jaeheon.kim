package org.sopt.repository.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.sopt.domain.Post;
import org.sopt.domain.enums.Tag;

import java.util.List;
import java.util.Optional;

import static org.sopt.constant.JpqlConstant.*;

public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final EntityManager entityManager;

    public CustomPostRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Post> searchPosts(Optional<String> keyword, String target, Optional<Tag> tag) {
        StringBuilder dynamicQuery = new StringBuilder(
                """
                        select p
                        from Post p
                        inner join fetch p.user u
                        where 1=1
                        """);

        if (keyword.isPresent()) {
            if (target.equals("writer")) {
                dynamicQuery.append(AND)
                        .append("u.name").append(LIKE).append(likePattern(keyword.get()));
            }
            if (target.equals("title")) {
                dynamicQuery.append(AND)
                        .append("p.title").append(LIKE).append(likePattern(keyword.get()));
            }
        }

        tag.ifPresent((t) -> dynamicQuery.append(AND)
                .append("p.tag = ").append(t));

        dynamicQuery.append(NEW_LINE)
                .append(ORDER_BY).append("p.createdAt").append(DESC);

        Query query = entityManager.createQuery(dynamicQuery.toString());
        return query.getResultList();
    }

    private String likePattern(String keyword) {
        return "'%" + keyword + "%'";
    }

}
