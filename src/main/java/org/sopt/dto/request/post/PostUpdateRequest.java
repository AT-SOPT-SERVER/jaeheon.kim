package org.sopt.dto.request.post;

import java.util.Optional;

public record PostUpdateRequest(Optional<String> title,
                                Optional<String> content) {
}
