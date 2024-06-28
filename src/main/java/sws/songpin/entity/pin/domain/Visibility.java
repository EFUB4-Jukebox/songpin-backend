package sws.songpin.entity.pin.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import sws.songpin.entity.member.domain.Status;

public enum Visibility {
    PUBLIC,
    PRIVATE
    ;

    @JsonCreator
    public static Visibility from(String s) {
        return Visibility.valueOf(s.toUpperCase());
    }
}