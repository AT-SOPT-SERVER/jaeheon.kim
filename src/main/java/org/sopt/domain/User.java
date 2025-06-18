package org.sopt.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.sopt.domain.base.BaseEntity;
import org.sopt.exception.ForbiddenException;
import org.sopt.exception.errorcode.ErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {
	@Serial
	private final static long serialVersionUID = 4L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;
	private String email;

	protected User() {
	}

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void checkIsWriter(User writer, ErrorCode errorCode) {
		if (!this.equals(writer)) {
			throw new ForbiddenException(errorCode);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User that)) {
			return false;
		}
		return Objects.equals(this.getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}
