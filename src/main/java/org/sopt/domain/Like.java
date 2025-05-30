package org.sopt.domain;

import org.sopt.domain.base.BaseEntity;
import org.sopt.domain.id.LikeId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "content_like")
public class Like extends BaseEntity {
	@Id
	private LikeId id;

	protected Like() {
	}

	protected Like(LikeId id) {
		this.id = id;
	}

}
