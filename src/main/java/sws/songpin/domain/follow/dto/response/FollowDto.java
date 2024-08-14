package sws.songpin.domain.follow.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record FollowDto(
        Long memberId,
        ProfileImg profileImg,
        String nickname,
        String handle,
        Boolean isFollowing
) {
    public static FollowDto from (Member member, Boolean isFollowing) {
        return new FollowDto(
                member.getMemberId(),
                member.getProfileImg(),
                member.getNickname(),
                member.getHandle(),
                isFollowing
        );
    }
}