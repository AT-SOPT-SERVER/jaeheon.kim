package org.sopt.domain;

import org.sopt.domain.base.BaseEntity;
import org.sopt.domain.enums.ContentType;
import org.sopt.domain.id.LikeId;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.errorcode.ErrorCode;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "content_like")
public class Like extends BaseEntity {
	/**
	 * Content라는 인터페이스를 작성하고 (InheritanceType.JOIN) 을 사용한다면, 그래서 이걸 Like 가 생길 수 있는 도메인이 상속받도록 한다면, 연관관계를 잘 가져갈 수 있을 것 같음.
	 */
	@Id
	private LikeId id;

	protected Like() {
	}

	protected Like(LikeId id) {
		this.id = id;
	}

	public static Like createLike(User user, Object object) {
		if (object instanceof Post post)
			return createLike(user.getId(), post.getId(), post);
		if (object instanceof Comment comment)
			return createLike(user.getId(), comment.getId(), comment);

		throw new BadRequestException(ErrorCode.NOT_EXIST_CONTENT_TYPE);
	}

	private static Like createLike(Long userId, Long contentId, Object object) {
		LikeId likeId = new LikeId(userId, contentId, ContentType.fromType(object));
		return new Like(likeId);
	}
}
