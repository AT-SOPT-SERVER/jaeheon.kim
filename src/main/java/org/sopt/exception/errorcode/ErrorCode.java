package org.sopt.exception.errorcode;

public enum ErrorCode {

	LENGTH("4000", "제한 길이를 초과했습니다."),
	NOT_BLANK("4001", "공백이 불가합니다."),
	NOT_NULL("4002", "필수 요소는 생략할 수 없습니다."),
	NOT_EXIST_TAG("4003", "잘못된 태그입니다."),
	NOT_EXIST_CONTENT_TYPE("4004", "잘못된 컨텐츠 타입입니다."),

	NOT_ALLOWED_POST("4030", "해당 POST 에 대한 권한이 없습니다."),
	NOT_ALLOWED_USER("4031", "해당 USER 에 대한 권한이 없습니다."),
	NOT_ALLOWED_COMMENT("4032", "해당 COMMENT 에 대한 권한이 없습니다."),

	POST_NOT_FOUND("4040", "해당 POST 를 찾을 수 없습니다."),
	USER_NOT_FOUND("4041", "해당 USER 를 찾을 수 없습니다."),
	COMMENT_NOT_FOUND("4042", "해당 COMMENT 를 찾을 수 없습니다."),

	POST_TITLE_CONFLICT("4090", "이미 존재하는 POST TITLE 입니다.");

	private final String statusCode;
	private final String message;

	ErrorCode(String statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public String getStatusCode() {
		return this.statusCode;
	}

	public String getMessage() {
		return this.message;
	}
}
