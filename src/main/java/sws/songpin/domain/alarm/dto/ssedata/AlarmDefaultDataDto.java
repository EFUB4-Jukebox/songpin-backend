package sws.songpin.domain.alarm.dto.ssedata;

import sws.songpin.domain.member.entity.Member;

public record AlarmDefaultDataDto(
        Long memberId
) {
    public static AlarmDefaultDataDto from (Member member) {
        return new AlarmDefaultDataDto(
                member.getMemberId()
        );
    }
}