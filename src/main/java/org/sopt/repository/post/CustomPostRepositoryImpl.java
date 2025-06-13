package org.sopt.repository.post;

import static org.sopt.domain.QLike.*;
import static org.sopt.domain.QPost.*;
import static org.sopt.domain.QPostTag.*;
import static org.sopt.domain.QTag.*;
import static org.sopt.domain.QUser.*;

import java.util.List;
import java.util.Optional;

import org.sopt.domain.User;
import org.sopt.domain.enums.ContentType;
import org.sopt.dto.response.post.PostPreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomPostRepositoryImpl implements CustomPostRepository {

	private final DynamicQueryProvider dynamicQueryProvider;
	private final JPAQueryFactory jpaQueryFactory;

	public CustomPostRepositoryImpl(DynamicQueryProvider dynamicQueryProvider, JPAQueryFactory jpaQueryFactory) {
		this.dynamicQueryProvider = dynamicQueryProvider;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public Page<PostPreviewResponse> searchPosts(
		Optional<String> keyword,
		String target,
		Optional<Long> tagId,
		int page,
		int size,
		Optional<User> requestUser
	) {
		BooleanExpression whereCondition = Expressions.allOf(
			dynamicQueryProvider.searchByKeyword(keyword, target),
			dynamicQueryProvider.searchByTagId(tagId)
		);

		JPAQuery<PostPreviewResponse> query = getSearchPostsQueryWithJoin(requestUser);
		List<PostPreviewResponse> postPreviewResponses = getSearchPostsPreviews(query, whereCondition, page, size);

		long count = getSearchPostsCount(whereCondition);
		return new PageImpl<>(postPreviewResponses, PageRequest.of(page, size), count);
	}

	private long getSearchPostsCount(BooleanExpression whereCondition) {
		return jpaQueryFactory
			.select(post.count())
			.from(post)
			.leftJoin(postTag).on(postTag.post.eq(post))
			.where(whereCondition)
			.fetchOne();
	}

	private JPAQuery<PostPreviewResponse> getSearchPostsQueryWithJoin(Optional<User> requestUser) {
		JPAQuery<PostPreviewResponse> query = jpaQueryFactory
			.selectDistinct(Projections.constructor(
				PostPreviewResponse.class,
				post,
				requestUser.isPresent() ? like.isNotNull() : Expressions.FALSE))
			.from(post)
			.leftJoin(postTag).on(postTag.post.eq(post)).fetchJoin()
			.leftJoin(tag).on(postTag.tag.eq(tag)).fetchJoin()
			.innerJoin(user).on(post.user.eq(user)).fetchJoin();

		requestUser.ifPresent(u -> {
			query.leftJoin(like).on(like.id.contentId.eq(post.id)
				.and(like.id.contentType.eq(ContentType.POST))
				.and(like.id.userId.eq(u.getId())));
		});

		return query;
	}

	public List<PostPreviewResponse> getSearchPostsPreviews(
		JPAQuery<PostPreviewResponse> query,
		BooleanExpression whereCondition,
		int page,
		int size
	) {
		return query
			.where(whereCondition)
			.orderBy(post.createdAt.desc())
			.limit(size)
			.offset((long)size * page)
			.fetch();
	}

}
