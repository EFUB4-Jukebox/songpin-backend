package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.follow.service.FollowService;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.pin.dto.response.FeedPinListResponseDto;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.playlist.service.PlaylistService;

@Tag(name = "Member", description = "Member 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final PlaylistService playlistService;
    private final PinService pinService;
    private final FollowService followService;

    @Operation(summary = "유저 검색", description = "유저를 검색합니다.")
    @GetMapping
    public ResponseEntity<?> searchMembers(@RequestParam final String keyword,
                                           @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(memberService.searchMembers(keyword, pageable));
    }

    @Operation(summary = "타 유저의 플레이리스트 목록 조회", description = "타 유저의 페이지에서 플레이리스트 목록 조회")
    @GetMapping("/{memberId}/playlists")
    public ResponseEntity<?> getAllPlaylists(@PathVariable("memberId") final Long memberId){
        return ResponseEntity.ok(playlistService.getAllPlaylists(memberId));
    }

    @GetMapping("/{memberId}/pins")
    @Operation(summary = "타 유저의 공개 핀 피드 조회", description = "타 유저의 공개 핀 피드를 조회합니다.")
    public ResponseEntity<?> getPublicFeedPins(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(pinService.getPublicFeedPins(memberId));
    }

    @Operation(summary = "유저의 팔로잉 목록 조회", description = "유저의 팔로잉 목록을 불러옵니다.")
    @GetMapping("/{memberId}/followings")
    public ResponseEntity<?> followingList(@PathVariable final Long memberId) {
        return ResponseEntity.ok(followService.getFollowList(memberId, true));
    }

    @Operation(summary = "유저의 팔로워 목록 조회", description = "유저의 팔로워 목록을 불러옵니다.")
    @GetMapping("/{memberId}/followers")
    public ResponseEntity<?> followerList(@PathVariable final Long memberId) {
        return ResponseEntity.ok(followService.getFollowList(memberId, false));
    }
}
