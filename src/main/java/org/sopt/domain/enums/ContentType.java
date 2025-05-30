package org.sopt.domain.enums;

import org.sopt.exception.BadRequestException;
import org.sopt.exception.errorcode.ErrorCode;

public enum ContentType {

	POST("post", "게시글"),
	COMMENT("content", "댓글");

	private final String value;
	private final String description;

	ContentType(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public static ContentType resolveContentType(String value) {
		return switch (value) {
			case "post" -> POST;
			case "content" -> COMMENT;
			default -> throw new BadRequestException(ErrorCode.NOT_EXIST_CONTENT_TYPE);
		};
	}

}
