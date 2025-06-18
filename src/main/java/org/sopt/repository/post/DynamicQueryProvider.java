package org.sopt.repository.post;

import static org.sopt.domain.QPost.*;
import static org.sopt.domain.QPostTag.*;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;

@Component
public class DynamicQueryProvider {

	public BooleanExpression searchByKeyword(Optional<String> keyword, String target) {

		return keyword
			.map(searchedKeyword -> switch (target) {
				case "title" -> post.title.contains(searchedKeyword.trim());
				case "writer" -> post.user.name.contains(searchedKeyword.trim());
				default -> null;
			})
			.orElse(null);
	}

	public BooleanExpression searchByTagId(Optional<Long> tagId) {
		return tagId
			.map(postTag.tag.id::eq)
			.orElse(null);
	}

}
