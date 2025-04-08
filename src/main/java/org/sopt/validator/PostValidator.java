package org.sopt.validator;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequest;

public class PostValidator {
    public static final Integer MAX_TITLE_SIZE = 30;

    public static boolean isValidTitle(String title){
        if (title.length() > MAX_TITLE_SIZE
                || title.isBlank()){
            return false;
        }
        return true;
    }
}
