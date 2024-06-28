package sws.songpin.entity.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    ACTIVE,
    DELETED
    ;

    @JsonCreator
    public static Status from(String s) {
        return Status.valueOf(s.toUpperCase());
    }
}