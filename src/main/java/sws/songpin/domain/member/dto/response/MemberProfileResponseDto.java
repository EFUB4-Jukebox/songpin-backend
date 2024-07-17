package sws.songpin.domain.member.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record MemberProfileResponseDto(
        ProfileImg profileImg,
        String nickname,
        String handle,
        long followerCount,
        long followingCount,
        Long followId
) {
    public static MemberProfileResponseDto from(Member member, long followerCount, long followingCount, Long followId){
        return new MemberProfileResponseDto(member.getProfileImg(), member.getNickname(), member.getHandle(), followerCount, followingCount, followId);
    }
}
