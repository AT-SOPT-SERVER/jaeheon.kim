package org.sopt.exception;

import org.sopt.exception.errorcode.ErrorCode;

public class UnAuthorizedException extends CustomException {
	public UnAuthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
