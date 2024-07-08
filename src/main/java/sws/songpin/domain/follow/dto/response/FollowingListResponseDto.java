package sws.songpin.domain.follow.dto.response;

import java.util.List;

public record FollowingListResponseDto(
    boolean isMe,
    String handle,
    List<FollowDto> followingList
) {
    public static FollowingListResponseDto from(boolean isMe, String handle, List<FollowDto> followDtoList) {
        return new FollowingListResponseDto(isMe, handle, followDtoList);
    }
}