package sws.songpin.domain.genre.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GenreName {
    POP, // 팝
    ROCK, // 락&메탈
    BALLAD, // 발라드
    JAZZ, // 재즈
    HIPHOP, // 힙합
    LOFI, // 로파이
    DANCE, // 댄스
    EXTRA // 나머지
    ;

    // ex. JSON 데이터 "POP"라는 문자열 -> GenreName.POP으로 변환
    @JsonCreator
    public static GenreName from(String s) {
        return GenreName.valueOf(s.toUpperCase());
    }
}
