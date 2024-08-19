package sws.songpin.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

public enum Status {
    ACTIVE,
    DELETED
    ;

    @JsonCreator
    public static Status from(String s) {
        try {
            return Status.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}