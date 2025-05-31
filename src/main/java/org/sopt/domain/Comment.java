package org.sopt.domain;

import org.sopt.domain.base.BaseEntity;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.errorcode.ErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 300, nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	protected Comment() {
	}

	public Comment(Post post, User user, String content) {
		this.content = content;
		this.user = user;
		this.post = post;
	}

	public Long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public User getUser() {
		return user;
	}

	public Post getPost() {
		return post;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void checkPostIdIntegrity(Long postId) {
		if (!this.getPost().getId().equals(postId)) {
			throw new BadRequestException(ErrorCode.INVALID_POST_REQUEST, "댓글이 요청된 게시물과 일치하지 않습니다.");
		}
	}
}
