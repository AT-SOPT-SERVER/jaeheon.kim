package org.sopt.exception;

import org.sopt.exception.errorcode.ErrorCode;

import java.util.Optional;

public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Optional<String> detail;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.detail = Optional.empty();
    }

    public CustomException(ErrorCode errorCode, String detail) {
        this.errorCode = errorCode;
        this.detail = Optional.ofNullable(detail);
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String getDetail() {
        return this.detail.orElse(null);
    }
}
