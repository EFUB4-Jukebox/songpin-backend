package sws.songpin.domain.member.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record MemberProfileResponseDto(
        Long memberId,
        ProfileImg profileImg,
        String nickname,
        String handle,
        long followerCount,
        long followingCount,
        Boolean isFollowing,
        Boolean isFollower
) {
    public static MemberProfileResponseDto from(Member member, long followerCount, long followingCount, Boolean isFollowing, Boolean isFollower){
        return new MemberProfileResponseDto(member.getMemberId(), member.getProfileImg(), member.getNickname(), member.getHandle(), followerCount, followingCount, isFollowing, isFollower);
    }
}
