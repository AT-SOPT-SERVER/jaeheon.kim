package org.sopt.validator;

import org.sopt.exception.ValidationException;
import org.sopt.exception.errorcode.ErrorCode;

import java.text.BreakIterator;

import static org.sopt.constant.CommonConstant.ZERO_WIDTH_JOINER;
import static org.sopt.constant.PostConstant.MAX_TITLE_SIZE;

public class PostValidator {

    public static void validTitle(String title) {
        if (title == null)
            throw new ValidationException(ErrorCode.NOT_NULL, "title 은 필수 요소 입니다.");
        if (title.isBlank())
            throw new ValidationException(ErrorCode.NOT_BLANK, "title 은 공백일 수 없습니다.");
        if (!PostValidator.isValidTitleLength(title))
            throw new ValidationException(ErrorCode.LENGTH, "title 의 길이를 확인해주세요.");
    }

    public static boolean isValidTitleLength(String title) {
        return isLessThanMaxTitleSize(title);
    }

    private static boolean isLessThanMaxTitleSize(String title) {
        BreakIterator textIterator = BreakIterator.getCharacterInstance();
        textIterator.setText(title);
        int cnt = 0;

        while (textIterator.next() != BreakIterator.DONE) {
            char currentChar = textIterator.getText().current();
            if (currentChar == ZERO_WIDTH_JOINER) {
                cnt--;
                continue;
            }
            cnt++;
            if (cnt > MAX_TITLE_SIZE) {
                return false;
            }
        }
        return true;
    }
}
