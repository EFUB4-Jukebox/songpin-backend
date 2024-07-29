package sws.songpin.domain.member.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record MyProfileResponseDto(
        ProfileImg profileImg,
        String nickname,
        String handle,
        String email,
        long followerCount,
        long followingCount
) {
    public static MyProfileResponseDto from(Member member, long followerCount, long followingCount){
        return new MyProfileResponseDto(member.getProfileImg(), member.getNickname(), member.getHandle(), member.getEmail(), followerCount, followingCount);
    }
}
