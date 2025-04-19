package org.sopt.dto.request.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.validator.PostValidator;

public record PostRequest(String title) {

    public PostRequest(@JsonProperty(value = "title") String title) {
        if (!PostValidator.isValidTitle(title)) {
            throw new BadRequestException(ErrorCode.LENGTH);
        }
        this.title = title;
    }
}
