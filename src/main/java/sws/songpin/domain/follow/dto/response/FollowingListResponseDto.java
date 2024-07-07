package sws.songpin.domain.follow.dto.response;

import sws.songpin.domain.follow.dto.FollowDto;

import java.util.List;

public record FollowingListResponseDto(
        boolean isMe,
        String handle,
        List<FollowDto> followingList
) {
    public static FollowingListResponseDto fromEntity(boolean isMe, String handle, List<FollowDto> followDtoList) {
        return new FollowingListResponseDto(isMe, handle, followDtoList);
    }
}