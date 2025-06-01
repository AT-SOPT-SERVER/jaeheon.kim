package org.sopt.dto.request.post;

import java.util.List;

public record PostCreateRequest(String title,
								String content,
								List<Long> tagIds) {
}
