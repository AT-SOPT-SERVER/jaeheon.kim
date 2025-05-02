package org.sopt.util.validator;

import org.sopt.exception.ValidationException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.util.StringUtil;

import static org.sopt.constant.PostConstant.POST_MAX_CONTENT_SIZE;
import static org.sopt.constant.PostConstant.POST_MAX_TITLE_SIZE;

public class PostValidator {

    public static void validTitle(String title) {
        if (title == null)
            throw new ValidationException(ErrorCode.NOT_NULL, "title 은 필수 요소 입니다.");
        if (title.isBlank())
            throw new ValidationException(ErrorCode.NOT_BLANK, "title 은 공백일 수 없습니다.");
        if (StringUtil.lengthWithEmoji(title) > POST_MAX_TITLE_SIZE)
            throw new ValidationException(ErrorCode.LENGTH, "title 의 최대 길이를 초과했습니다.");
    }

    public static boolean isValidTitleLength(String title) {
        return isLessThanMaxTitleSize(title);
    }

}
