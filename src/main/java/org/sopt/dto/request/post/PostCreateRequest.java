package org.sopt.dto.request.post;

public record PostCreateRequest(String title,
                                String content,
                                String tag) {
}
