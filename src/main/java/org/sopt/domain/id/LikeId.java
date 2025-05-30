package org.sopt.domain.id;

import java.io.Serializable;

import org.sopt.domain.enums.ContentType;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class LikeId implements Serializable {
	private Long contentId;
	private Long UserId;
	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	protected LikeId() {
	}

	protected LikeId(Long contentId, Long userId, ContentType contentType) {
		this.contentId = contentId;
		UserId = userId;
		this.contentType = contentType;
	}
}
