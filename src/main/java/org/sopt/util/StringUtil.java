package org.sopt.util;

import java.text.BreakIterator;

import static org.sopt.constant.CommonConstant.ZERO_WIDTH_JOINER;

public class StringUtil {

    /**
     * 이모지를 글자 하나로 취급하여 글자수를 계산하는 메서드.
     * 현재는 이모지 수정자를 처리하지 못하는 문제가 존재
     *
     * @param input
     * @return
     */
    public static int lengthWithEmoji(String input) {
        BreakIterator textIterator = BreakIterator.getCharacterInstance();
        textIterator.setText(input);
        int cnt = 0;

        while (textIterator.next() != BreakIterator.DONE) {
            char currentChar = textIterator.getText().current();
            if (currentChar == ZERO_WIDTH_JOINER) {
                cnt--;
                continue;
            }
            cnt++;
        }
        return cnt;
    }

    private static boolean isSkinToneModifier(int codePoint) {
        return codePoint >= 0x1F3FB && codePoint <= 0x1F3FF;
    }
}
