package org.sopt.dto.request.post;

import java.util.Optional;

public record PostCreateRequest(String title,
                                String content,
                                Optional<String> tag) {
}
