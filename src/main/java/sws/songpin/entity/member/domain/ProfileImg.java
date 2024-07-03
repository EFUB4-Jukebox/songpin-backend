package sws.songpin.entity.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProfileImg {
    ROCK, // 락&메탈
    BALLAD, // 발라드
    POP, // 팝
    JAZZ, // 재즈
    HIPHOP, // 힙합
    LOFI, // 로파이
    DANCE, // 댄스
    EXTRA // 나머지
    ;

    @JsonCreator
    public static ProfileImg from(String s) {
        return ProfileImg.valueOf(s.toUpperCase());
    }
}
