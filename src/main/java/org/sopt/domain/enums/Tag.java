package org.sopt.domain.enums;

import org.sopt.exception.BadRequestException;
import org.sopt.exception.errorcode.ErrorCode;

public enum Tag {

    BACKEND("backend"),
    DATABASE("database"),
    INFRASTRUCTURE("infrastructure");

    private final String value;

    Tag(String value) {
        this.value = value;
    }

    public static Tag resolveTag(String value) {
        return switch (value) {
            case "backend" -> BACKEND;
            case "database" -> DATABASE;
            case "infrastructure" -> INFRASTRUCTURE;
            default -> throw new BadRequestException(ErrorCode.NOT_EXIST_TAG);
        };
    }
}
