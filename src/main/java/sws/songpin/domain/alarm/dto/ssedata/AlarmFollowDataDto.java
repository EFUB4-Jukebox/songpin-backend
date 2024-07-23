package sws.songpin.domain.alarm.dto.ssedata;

import sws.songpin.domain.alarm.entity.AlarmType;
import sws.songpin.domain.member.entity.Member;

public record AlarmFollowDataDto(
        AlarmType alarmType,
        Long senderId,
        String senderNickname,
        String senderHandle
) {
    public static AlarmFollowDataDto from (Member member) {
        return new AlarmFollowDataDto(
                AlarmType.FOLLOW,
                member.getMemberId(),
                member.getNickname(),
                member.getHandle()
        );
    }
}