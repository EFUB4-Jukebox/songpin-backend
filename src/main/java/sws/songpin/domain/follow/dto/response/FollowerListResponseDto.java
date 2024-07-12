package sws.songpin.domain.follow.dto.response;

import java.util.List;

public record FollowerListResponseDto(
    Boolean isMe,
    String handle,
    List<FollowDto> followingList
) {
    public static FollowerListResponseDto from(Boolean isMe, String handle, List<FollowDto> followDtoList) {
        return new FollowerListResponseDto(isMe, handle, followDtoList);
    }
}