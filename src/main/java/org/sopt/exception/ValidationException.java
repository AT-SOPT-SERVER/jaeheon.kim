package org.sopt.exception;

import org.sopt.exception.errorcode.ErrorCode;

public class ValidationException extends CustomException {


    public ValidationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
