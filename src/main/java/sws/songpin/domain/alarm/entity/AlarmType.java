package sws.songpin.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    FOLLOW("{0}(@{1})님이 팔로우했어요."),
    REPORT("{0}(@{1})님에 대한 신고가 접수되었어요."),
    DEFAULT("{0}(@{1})님이 보낸 알림이에요.")
    ;

    private final String messagePattern;
}
