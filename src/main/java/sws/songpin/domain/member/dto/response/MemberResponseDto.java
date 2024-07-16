package sws.songpin.domain.member.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record MemberResponseDto(
        ProfileImg profileImg,
        String nickname,
        String handle,
        long follower,
        long following,
        Long followId
) {
    public static MemberResponseDto from(Member member, long followerCount, long followingCount, Long followId){
        return new MemberResponseDto(member.getProfileImg(), member.getNickname(), member.getHandle(), followerCount, followingCount, followId);
    }
}
