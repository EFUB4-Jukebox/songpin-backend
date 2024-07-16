package sws.songpin.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

public enum Visibility {
    PUBLIC,
    PRIVATE
    ;

    @JsonCreator
    public static Visibility from(String s) {
        try {
            return Visibility.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}