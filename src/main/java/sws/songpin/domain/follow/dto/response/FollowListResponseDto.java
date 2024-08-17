package sws.songpin.domain.follow.dto.response;

import java.util.List;

public record FollowListResponseDto(
        Boolean isMe,
        String handle,
        List<FollowUnitDto> followList
) {
    public static FollowListResponseDto from(Boolean isMe, String handle, List<FollowUnitDto> followDtoList) {
        return new FollowListResponseDto(isMe, handle, followDtoList);
    }
}