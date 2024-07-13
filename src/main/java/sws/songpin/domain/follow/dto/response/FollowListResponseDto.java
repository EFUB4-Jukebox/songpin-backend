package sws.songpin.domain.follow.dto.response;

import java.util.List;

public record FollowListResponseDto(
    Boolean isMe,
    String handle,
    List<FollowDto> followingList
) {
    public static FollowListResponseDto from(Boolean isMe, String handle, List<FollowDto> followDtoList) {
        return new FollowListResponseDto(isMe, handle, followDtoList);
    }
}