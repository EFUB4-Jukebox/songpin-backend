package sws.songpin.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum ReportType {

    SPAM("스팸/홍보"),
    FLOODING("도배"),
    ABUSE("욕설/비방"),
    DOXXING("개인정보 노출"),
    OFFENSIVE("불쾌감 조성"),
    OTHER("기타")
    ;

    private final String message;

    @JsonCreator
    public static ReportType from(String s) {
        try {
            return ReportType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
