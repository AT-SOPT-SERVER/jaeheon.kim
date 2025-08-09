package org.sopt.domain.enums;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.errorcode.ErrorCode;

public enum ContentType {

	POST("post", "게시글", Post.class),
	COMMENT("content", "댓글", Comment.class);

	private final String value;
	private final String description;
	private final Class<?> type;

	ContentType(String value, String description, Class<?> type) {
		this.value = value;
		this.description = description;
		this.type = type;
	}

	public static ContentType resolveContentType(String value) {
		return switch (value) {
			case "post" -> POST;
			case "content" -> COMMENT;
			default -> throw new BadRequestException(ErrorCode.NOT_EXIST_CONTENT_TYPE);
		};
	}

	public static ContentType fromType(Object o) {
		for (ContentType c : values()) {
			if (c.type.isInstance(o)) {
				return c;
			}
		}
		throw new BadRequestException(ErrorCode.NOT_EXIST_CONTENT_TYPE);
	}

}
