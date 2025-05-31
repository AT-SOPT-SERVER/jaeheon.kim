package org.sopt.domain.id;

import java.io.Serializable;
import java.util.Objects;

import org.sopt.domain.enums.ContentType;

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

	public LikeId(Long userId, Long contentId, ContentType contentType) {
		this.userId = userId;
		this.contentId = contentId;
		this.contentType = contentType;
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
