package sws.songpin.domain.alarm.dto.ssedata;

import sws.songpin.domain.member.entity.Member;

public record AlarmFollowDataDto(
        Long senderId,
        String senderNickname,
        String senderHandle
) {
    public static AlarmFollowDataDto from (Member member) {
        return new AlarmFollowDataDto(
                member.getMemberId(),
                member.getNickname(),
                member.getHandle()
        );
    }
}