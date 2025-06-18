package org.sopt.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.hibernate.Hibernate;
import org.sopt.domain.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(
	indexes = {
		@Index(name = "uk_title", columnList = "title", unique = true),
		@Index(name = "idx_created_at", columnList = "created_at")
	}
)
public class Post extends BaseEntity implements Serializable {

	@Serial
	private final static long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
	private final List<PostTag> postTags = new ArrayList<>();

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

	private int likeCount = 0;

	private int commentCount = 0;

	private Post(User user, String title, String content, Integer likeCount, Integer commentCount) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
	}

	protected Post() {
	}

	public static Post createNew(User user, String title, String content) {
		return new Post(user, title, content, 0, 0);
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void increaseCommentCount() {
		this.commentCount++;
	}

	public List<PostTag> getPostTags() {
		return postTags;
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

	public int getLikeCount() {
		return likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void updateTitle(String newTitle) {
		this.title = newTitle;
	}

	public void updateContent(String newContent) {
		this.content = newContent;
	}

	public void addPostTags(Collection<PostTag> postTags) {
		this.postTags.addAll(postTags);
	}

	public boolean isRelatedEntityProxy() {
		return !Hibernate.isInitialized(user)
			|| !Hibernate.isInitialized(postTags)
			|| isPostTagsTagProxy();
	}

	public boolean isPostTagsTagProxy() {
		return postTags.stream()
			.anyMatch(PostTag::isTagProxy);
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
