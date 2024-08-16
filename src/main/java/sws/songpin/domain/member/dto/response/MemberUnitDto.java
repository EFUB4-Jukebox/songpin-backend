package sws.songpin.domain.member.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record MemberUnitDto(
        String nickname,
        String handle,
        ProfileImg profileImg,
        Boolean isMe
) {
    public static MemberUnitDto from (Member member, Boolean isMe) {
        return new MemberUnitDto(
                member.getNickname(),
                member.getHandle(),
                member.getProfileImg(),
                isMe
        );
    }
}
