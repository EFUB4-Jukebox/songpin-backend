package sws.songpin.domain.follow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.follow.dto.request.FollowAddRequestDto;
import sws.songpin.domain.follow.service.FollowService;

@Tag(name = "Follow", description = "Follow 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "타 유저를 팔로잉", description = "다른 유저를 팔로잉합니다.")
    @PostMapping
    public ResponseEntity<?> followAdd(@RequestBody @Valid FollowAddRequestDto followAddRequestDto) {
        return ResponseEntity.ok(followService.addFollow(followAddRequestDto));
    }

    @Operation(summary = "팔로잉 취소 또는 팔로워 삭제", description = "나의 팔로잉을 취소하거나 팔로워를 삭제합니다.")
    @DeleteMapping("/{followId}")
    public ResponseEntity<?> followRemove(@PathVariable final Long followId) {
        followService.deleteFollow(followId);
        return ResponseEntity.noContent().build();
    }
}
