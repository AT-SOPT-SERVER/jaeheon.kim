package org.sopt.dto.response.post;

import org.springframework.data.domain.Page;

public record PostPreviewResponses(Page<PostPreviewResponse> responseList) {
}
