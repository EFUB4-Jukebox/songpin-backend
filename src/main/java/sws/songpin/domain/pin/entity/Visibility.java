package sws.songpin.domain.pin.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Visibility {
    PUBLIC,
    PRIVATE
    ;

    @JsonCreator
    public static Visibility from(String s) {
        return Visibility.valueOf(s.toUpperCase());
    }
}