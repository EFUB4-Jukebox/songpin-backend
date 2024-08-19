package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.follow.service.FollowService;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.playlist.service.PlaylistService;
import sws.songpin.domain.member.service.ProfileService;

@Tag(name = "Member", description = "Member 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final PlaylistService playlistService;
    private final PinService pinService;
    private final FollowService followService;
    private final ProfileService profileService;

    @Operation(summary = "유저 검색", description = "유저를 검색합니다.")
    @GetMapping
    public ResponseEntity<?> searchMembers(@RequestParam("keyword") final String keyword,
                                           @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(memberService.searchMembers(keyword, pageable));
    }
    @Operation(summary = "타 유저 정보 조회", description = "memberId으로 해당 유저의 프로필 이미지, 닉네임, 아이디 정보 조회")
    @GetMapping("/{handle}")
    public ResponseEntity<?> memberDetails(@PathVariable final String handle){
        return ResponseEntity.ok(profileService.getMemberProfile(handle));
    }

    @Operation(summary = "타 유저 플레이리스트 목록 조회", description = "타 유저 페이지에서 플레이리스트 목록 조회")
    @GetMapping("/{handle}/playlists")
    public ResponseEntity<?> getAllPlaylists(@PathVariable final String handle){
        return ResponseEntity.ok(playlistService.getMemberPlaylists(handle));
    }

    @Operation(summary = "타 유저의 핀 피드 조회", description = "타 유저의 핀 피드를 조회합니다.")
    @GetMapping("/{handle}/feed")
    public ResponseEntity<?> getMemberFeedPins(@PathVariable final String handle, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(pinService.getMemberPinFeed(handle, pageable));
    }

    @Operation(summary = "유저의 팔로잉 목록 조회", description = "유저의 팔로잉 목록을 불러옵니다.")
    @GetMapping("/{handle}/followings")
    public ResponseEntity<?> followingList(@PathVariable final String handle) {
        return ResponseEntity.ok(followService.getFollowList(handle, true));
    }

    @Operation(summary = "유저의 팔로워 목록 조회", description = "유저의 팔로워 목록을 불러옵니다.")
    @GetMapping("/{handle}/followers")
    public ResponseEntity<?> followerList(@PathVariable final String handle) {
        return ResponseEntity.ok(followService.getFollowList(handle, false));
    }
}
