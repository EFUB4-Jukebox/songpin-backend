package sws.songpin.domain.follow.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.follow.dto.request.FollowAddRequestDto;
import sws.songpin.domain.follow.service.FollowService;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @Tag(name = "유저의 팔로잉 목록 조회", description = "유저의 팔로잉 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/followings")
    public ResponseEntity<?> followingList(@PathVariable final Long memberId) {
        return ResponseEntity.ok(followService.getFollowingList(memberId));
    }

    @Tag(name = "유저의 팔로워 목록 조회", description = "유저의 팔로워 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/followers")
    public ResponseEntity<?> followerList(@PathVariable final Long memberId) {
        return ResponseEntity.ok(followService.getFollowerList(memberId));
    }

    @Tag(name = "타 유저를 팔로잉", description = "다른 유저를 팔로잉합니다.")
    @PostMapping("/follows")
    public ResponseEntity<?> followAdd(@Valid @RequestBody FollowAddRequestDto followAddRequestDto) {
        return ResponseEntity.ok(followService.addFollow(followAddRequestDto));
    }

    @Tag(name = "팔로잉 취소 또는 팔로워 삭제", description = "나의 팔로잉을 취소하거나 팔로워를 삭제합니다.")
    @DeleteMapping("/follows/{followId}")
    public ResponseEntity<?> followRemove(@PathVariable final Long followId) {
        followService.deleteFollow(followId);
        return ResponseEntity.noContent().build();
    }
}
