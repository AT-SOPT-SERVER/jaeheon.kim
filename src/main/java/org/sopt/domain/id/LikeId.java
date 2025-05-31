package org.sopt.domain.id;

import java.io.Serializable;
import java.util.Objects;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.domain.enums.ContentType;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.errorcode.ErrorCode;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class LikeId implements Serializable {

	private Long userId;
	private Long contentId;
	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	protected LikeId() {
	}

	protected LikeId(Long userId, Long contentId, ContentType contentType) {
		this.userId = userId;
		this.contentId = contentId;
		this.contentType = contentType;
	}

	public static LikeId generate(User user, Object object) {
		if (object instanceof Post post)
			return createLikeId(user.getId(), post.getId(), post);
		if (object instanceof Comment comment)
			return createLikeId(user.getId(), comment.getId(), comment);

		throw new BadRequestException(ErrorCode.NOT_EXIST_CONTENT_TYPE);
	}

	private static LikeId createLikeId(Long userId, Long contentId, Object object) {
		return new LikeId(userId, contentId, ContentType.fromType(object));
	}

	public Long getUserId() {
		return userId;
	}

	public Long getContentId() {
		return contentId;
	}

	public ContentType getContentType() {
		return contentType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof LikeId likeId))
			return false;

		return Objects.equals(userId, likeId.userId) &&
			Objects.equals(contentId, likeId.contentId) &&
			contentType.equals(likeId.contentType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, contentId, contentType);
	}
}
