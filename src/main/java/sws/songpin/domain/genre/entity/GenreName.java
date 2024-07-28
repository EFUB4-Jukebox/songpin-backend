package sws.songpin.domain.genre.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

public enum GenreName {
    POP, // 팝
    ROCK, // 록/메탈
    BALLAD, // 발라드
    JAZZ, // 재즈
    HIPHOP, // 힙합
    LOFI, // Lo-Fi
    DANCE, // 댄스
    EXTRA // 나머지
    ;

    // ex. JSON 데이터 "POP"라는 문자열 -> GenreName.POP으로 변환
    @JsonCreator
    public static GenreName from(String s) {
        try {
            return GenreName.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
