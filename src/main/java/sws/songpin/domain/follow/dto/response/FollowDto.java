package sws.songpin.domain.follow.dto.response;

import sws.songpin.domain.member.entity.ProfileImg;

public record FollowDto( // FollowerListResponseDto, FollowingListResponseDto에서 사용하는 레코드
    Long memberId,
    ProfileImg profileImg,
    String nickname,
    String handle,
    Long myFollowId // null이면 currentMember가 팔로잉 중이 아님
) {}