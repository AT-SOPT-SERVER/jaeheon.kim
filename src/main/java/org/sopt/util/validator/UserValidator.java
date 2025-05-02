package org.sopt.util.validator;

import org.sopt.exception.ValidationException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.util.StringUtil;

import static org.sopt.constant.UserConstant.USER_MAX_NAME_SIZE;

public class UserValidator {

    public static void validName(String name) {
        if (name == null)
            throw new ValidationException(ErrorCode.NOT_NULL, "name 은 필수 요소 입니다.");
        if (StringUtil.lengthWithEmoji(name) > USER_MAX_NAME_SIZE)
            throw new ValidationException(ErrorCode.LENGTH, "name 의 최대 길이를 초과했습니다.");
    }
}
