package org.sopt.dto.request.comment;

import java.util.Optional;

public record CommentUpdateRequest(Optional<String> content) {
}
