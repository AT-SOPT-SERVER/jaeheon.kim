package org.sopt.common.util.validator;

import static org.sopt.constant.CommentConstant.*;

import org.sopt.exception.ValidationException;
import org.sopt.exception.errorcode.ErrorCode;

public class CommentValidator {
	public static void validContent(String content) {
		if (content == null)
			throw new ValidationException(ErrorCode.NOT_NULL, "content 는 필수 요소 입니다.");
		if (content.isBlank())
			throw new ValidationException(ErrorCode.NOT_BLANK, "content 는 공백일 수 없습니다.");
		if (content.length() > COMMENT_MAX_CONTENT_SIZE)
			throw new ValidationException(ErrorCode.LENGTH, "content 의 최대 길이를 초과했습니다.");
	}
}
