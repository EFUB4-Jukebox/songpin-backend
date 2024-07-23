package sws.songpin.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    FOLLOW("{0}(@{1})님이 팔로우했습니다."),
    DEFAULT("{0}(@{1})님이 보낸 알림입니다.")
    ;

    private final String messagePattern;
}
