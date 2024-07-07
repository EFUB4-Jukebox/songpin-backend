package sws.songpin.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

public enum ProfileImg {
    POP, // 팝
    ROCK, // 락&메탈
    BALLAD, // 발라드
    JAZZ, // 재즈
    HIPHOP, // 힙합
    LOFI, // 로파이
    DANCE, // 댄스
    EXTRA // 나머지
    ;

    @JsonCreator
    public static ProfileImg from(String s) {
        try {
            return ProfileImg.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}