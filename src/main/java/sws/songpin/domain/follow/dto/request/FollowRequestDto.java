package sws.songpin.domain.follow.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.follow.entity.Follow;
import sws.songpin.domain.member.entity.Member;

public record FollowRequestDto(
        @NotNull String handle
){
    public static Follow toEntity(Member follower, Member following) {
        return Follow.builder()
                .follower(follower)
                .following(following)
                .build();
    }
}