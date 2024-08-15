package sws.songpin.domain.follow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.follow.dto.request.FollowRequestDto;
import sws.songpin.domain.follow.entity.Follow;
import sws.songpin.domain.follow.service.FollowService;

@Tag(name = "Follow", description = "Follow 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "팔로잉 상태 변경 혹은 팔로워 삭제", description = "다른 유저를 팔로잉 또는 팔로우 취소하거나, 나의 팔로워를 삭제합니다.")
    @PutMapping
    public ResponseEntity<?> changeFollow(@RequestBody @Valid FollowRequestDto requestDto) {
        boolean isCreated = followService.createOrDeleteFollow(requestDto);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
