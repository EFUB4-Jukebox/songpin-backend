package sws.songpin.domain.follow.dto.response;

import java.util.List;

public record FollowerListResponseDto(
        boolean isMe,
        String handle,
        List<FollowListDto> followingList
) {
    public static FollowerListResponseDto fromEntity(boolean isMe, String handle, List<FollowListDto> followerListDtos) {
        return new FollowerListResponseDto(isMe, handle, followerListDtos);
    }
}