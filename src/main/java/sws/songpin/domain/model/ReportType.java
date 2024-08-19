package sws.songpin.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

public enum ReportType {

    SPAM,
    FLOODING,
    ABUSE,
    DOXXING,
    OFFENSIVE,
    OTHER
    ;

    @JsonCreator
    public static ReportType from(String s) {
        try {
            return ReportType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
