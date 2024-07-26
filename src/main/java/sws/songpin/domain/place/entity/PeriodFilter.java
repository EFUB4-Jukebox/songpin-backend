package sws.songpin.domain.place.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

public enum PeriodFilter {
    WEEK, MONTH, THREE_MONTHS
    ;

    @JsonCreator
    public static PeriodFilter from(String s) {
        try {
            return PeriodFilter.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
