package org.sopt.domain.id;

import java.io.Serializable;

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
}
