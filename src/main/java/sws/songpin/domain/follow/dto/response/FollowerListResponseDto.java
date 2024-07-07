package sws.songpin.domain.follow.dto.response;

import java.util.List;

public record FollowerListResponseDto(
        boolean isMe,
        String handle,
        List<FollowDto> followingList
) {
    public static FollowerListResponseDto fromEntity(boolean isMe, String handle, List<FollowDto> followDtoList) {
        return new FollowerListResponseDto(isMe, handle, followDtoList);
    }
}