package sws.songpin.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

public enum SortBy {
    ACCURACY,
    COUNT,
    NEWEST
    ;

    @JsonCreator
    public static SortBy from(String s) {
        try {
            return SortBy.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}