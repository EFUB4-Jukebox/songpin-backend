package sws.songpin.domain.follow.dto.response;

import sws.songpin.domain.follow.entity.Follow;

public record FollowAddResponseDto(
        Long followId
){
    public static FollowAddResponseDto from(Follow follow) {
        return new FollowAddResponseDto(follow.getFollowId());
    }
}