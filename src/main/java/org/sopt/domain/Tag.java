package org.sopt.domain;

import org.sopt.domain.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tag extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String value;

	@Column(nullable = false)
	private String koreanName;

	public Tag(String value, String koreanName) {
		this.koreanName = koreanName;
		this.value = value;
	}

	protected Tag() {
	}

	public Long getId() {
		return id;
	}

	public String getKoreanName() {
		return koreanName;
	}

	public String getValue() {
		return value;
	}
}
