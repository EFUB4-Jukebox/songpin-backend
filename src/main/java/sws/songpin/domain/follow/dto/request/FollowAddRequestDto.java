package sws.songpin.domain.follow.dto.request;

import sws.songpin.domain.follow.entity.Follow;
import sws.songpin.domain.member.entity.Member;

public record FollowAddRequestDto(
        Long followerId,
        Long followingId
){
    public static Follow toEntity(Member follower, Member following) {
        return Follow.builder()
                .follower(follower)
                .following(following)
                .build();
    }
}