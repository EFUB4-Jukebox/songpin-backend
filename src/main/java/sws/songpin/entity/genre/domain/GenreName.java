package sws.songpin.entity.genre.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GenreName {
    ROCK, // 락&메탈
    BALLAD, // 발라드
    POP, // 팝
    JAZZ, // 재즈
    HIPHOP, // 힙합
    LOFI, // 로파이
    DANCE, // 댄스
    EXTRA // 나머지
    ;

    // ex. JSON 데이터 "ROCK"라는 문자열 -> GenreName.ROCK으로 변환
    @JsonCreator
    public static GenreName from(String s) {
        return GenreName.valueOf(s.toUpperCase());
    }
}
