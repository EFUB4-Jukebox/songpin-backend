package sws.songpin.domain.follow.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;

public record FollowDto( // FollowerListResponseDto, FollowingListResponseDto에서 사용하는 레코드
        Long memberId,
        ProfileImg profileImg,
        String nickname,
        String handle,
        Boolean isFollowing,
        Long followId
) {
    public static FollowDto from (Member member, Boolean isFollowing, Long followId) {
        return new FollowDto(
                member.getMemberId(),
                member.getProfileImg(),
                member.getNickname(),
                member.getHandle(),
                isFollowing,
                followId // currentMember가 팔로잉하는 경우 currentMember와의 followId 삽입
        );
    }
}