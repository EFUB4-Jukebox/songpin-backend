package sws.songpin.domain.follow.dto.response;

import java.util.List;

public record FollowingListResponseDto(
        boolean isMe,
        String handle,
        List<FollowListDto> followingList
) {
    public static FollowingListResponseDto fromEntity(boolean isMe, String handle, List<FollowListDto> followingListDtos) {
        return new FollowingListResponseDto(isMe, handle, followingListDtos);
    }
}