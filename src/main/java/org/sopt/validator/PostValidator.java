package org.sopt.validator;

import java.text.BreakIterator;

import static org.sopt.constant.CommonConstant.ZERO_WIDTH_JOINER;
import static org.sopt.constant.PostConstant.MAX_TITLE_SIZE;

public class PostValidator {

    public static boolean isValidTitle(String title){
        if(title.isBlank()
                || !isLessThanMaxTitleSize(title)){
            return false;
        }
        return true;
    }


    private static boolean isLessThanMaxTitleSize(String title){
        BreakIterator textIterator = BreakIterator.getCharacterInstance();
        textIterator.setText(title);
        int cnt = 0;

        while (textIterator.next() != BreakIterator.DONE){
            char currentChar = textIterator.getText().current();
            if (currentChar == ZERO_WIDTH_JOINER){
                cnt--;
                continue;
            }
            cnt++;
            if(cnt > MAX_TITLE_SIZE){
                return false;
            }
        }
        return true;
    }
}
