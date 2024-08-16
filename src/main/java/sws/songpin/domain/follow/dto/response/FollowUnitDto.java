package sws.songpin.domain.follow.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record FollowUnitDto(
        Long memberId,
        ProfileImg profileImg,
        String nickname,
        String handle,
        Boolean isFollowing
) {
    public static FollowUnitDto from (Member member, Boolean isFollowing) {
        return new FollowUnitDto(
                member.getMemberId(),
                member.getProfileImg(),
                member.getNickname(),
                member.getHandle(),
                isFollowing
        );
    }
}