package sws.songpin.domain.follow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.follow.dto.request.FollowAddRequestDto;
import sws.songpin.domain.follow.service.FollowSerivce;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowSerivce followService;

    @GetMapping("/members/{memberId}/followings")
    public ResponseEntity<?> followingList(@PathVariable final Long memberId) {
        return ResponseEntity.ok(followService.getFollowingList(memberId));
    }

    @GetMapping("/members/{memberId}/followers")
    public ResponseEntity<?> followerList(@PathVariable final Long memberId) {
        return ResponseEntity.ok(followService.getFollowerList(memberId));
    }

    @PostMapping("/follows")
    public ResponseEntity<?> followAdd(FollowAddRequestDto followAddRequestDto) {
        return ResponseEntity.ok(followService.addFollow(followAddRequestDto));
    }

    @DeleteMapping("/follows/{followId}")
    public ResponseEntity<?> followRemove(@PathVariable final Long followId) {
        followService.deleteFollow(followId);
        return ResponseEntity.noContent().build();
    }
}
