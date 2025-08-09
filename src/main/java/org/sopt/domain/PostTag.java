package org.sopt.domain;

import java.io.Serial;
import java.io.Serializable;

import org.hibernate.Hibernate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PostTag implements Serializable {

	@Serial
	private final static long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", nullable = false)
	private Tag tag;

	protected PostTag() {
	}

	private PostTag(Post post, Tag tag) {
		this.post = post;
		this.tag = tag;
	}

	public static PostTag create(Post post, Tag tag) {
		return new PostTag(post, tag);
	}

	public Long getId() {
		return id;
	}

	public Post getPost() {
		return post;
	}

	public Tag getTag() {
		return tag;
	}

	public boolean isTagProxy() {
		return !Hibernate.isInitialized(tag);
	}

}
