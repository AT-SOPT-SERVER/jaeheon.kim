package org.sopt.exception.errorcode;

public enum ErrorCode {

    POST_NOT_FOUND("4040", "해당 POST 를 찾을 수 없습니다.");

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
