package org.sopt.dto;

import org.sopt.exception.CustomException;
import org.sopt.exception.errorcode.ErrorCode;

public class ErrorResponseDto {
    private final String statusCode;
    private final String message;
    private final String detail;

    private ErrorResponseDto(String statusCode, String message, String detail) {
        this.statusCode = statusCode;
        this.message = message;
        this.detail = detail;
    }

    public static ErrorResponseDto from(final CustomException customException) {
        ErrorCode errorCode = customException.getErrorCode();
        return new ErrorResponseDto(errorCode.getStatusCode(),
                errorCode.getMessage(),
                customException.getDetail());
    }

    public static ErrorResponseDto of(final String statusCode, final Exception exception) {
        return new ErrorResponseDto(
                statusCode,
                exception.getMessage(),
                null);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
