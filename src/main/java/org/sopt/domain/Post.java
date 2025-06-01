package org.sopt.domain;

import java.util.Objects;

import org.sopt.domain.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
	indexes = {
		@Index(name = "uk_title", columnList = "title", unique = true)
	}
)
public class Post extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Column(length = 1000, nullable = false)
	private String content;

	public Post(User user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
	}

	protected Post() {
	}

	public Long getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public User getUser() {
		return user;
	}

	public String getContent() {
		return content;
	}

	public void updateTitle(String newTitle) {
		this.title = newTitle;
	}

	public void updateContent(String newContent) {
		this.content = newContent;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Post that)) {
			return false;
		}
		return Objects.equals(this.getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}
