package org.sopt.domain;

import java.util.Objects;

import org.sopt.domain.base.BaseEntity;
import org.sopt.domain.id.LikeId;

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
		return new Like(LikeId.generate(user, object));
	}

	public LikeId getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Like like))
			return false;
		return Objects.equals(id, like.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
