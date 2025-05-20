package org.sopt.util.validator;

import org.junit.jupiter.api.Test;
import org.sopt.common.util.validator.PostValidator;
import org.sopt.exception.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sopt.constant.PostConstant.POST_MAX_TITLE_SIZE;

class PostValidatorTest {

    @Test
    void titleIsNull() {
        assertThatThrownBy(() -> PostValidator.validTitle(null))
                .isInstanceOf(ValidationException.class)
                .satisfies(e -> {
                    ValidationException validationException = (ValidationException) e;
                    assertThat(validationException.getDetail()).isEqualTo("title 은 필수 요소 입니다.");
                });
    }

    @Test
    void titleIsBlank() {
        assertThatThrownBy(() -> PostValidator.validTitle(""))
                .isInstanceOf(ValidationException.class)
                .satisfies(e -> {
                    ValidationException validationException = (ValidationException) e;
                    assertThat(validationException.getDetail()).isEqualTo("title 은 공백일 수 없습니다.");
                });
    }

    @Test
    void outOfMaxTitleLength() {
        String title = "x" .repeat(POST_MAX_TITLE_SIZE + 1);

        assertThatThrownBy(() -> PostValidator.validTitle(title))
                .isInstanceOf(ValidationException.class)
                .satisfies(e -> {
                    ValidationException validationException = (ValidationException) e;
                    assertThat(validationException.getDetail()).isEqualTo("title 의 최대 길이를 초과했습니다.");
                });
    }

}