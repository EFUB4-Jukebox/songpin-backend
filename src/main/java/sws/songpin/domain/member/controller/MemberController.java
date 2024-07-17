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
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.playlist.service.PlaylistService;
import sws.songpin.domain.member.service.ProfileService;

@Tag(name = "Member", description = "Member 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final PlaylistService playlistService;
    private final ProfileService profileService;

    @Operation(summary = "유저 검색", description = "유저를 검색합니다.")
    @GetMapping
    public ResponseEntity<?> searchMembers(@RequestParam final String keyword,
                                           @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(memberService.searchMembers(keyword, pageable));
    }
    @Operation(summary = "타 유저 정보 조회", description = "memberId으로 해당 유저의 프로필 이미지, 닉네임, 아이디 정보 조회")
    @GetMapping("/{memberId}")
    public ResponseEntity<?> memberDetails(@PathVariable("memberId") final Long memberId){
        return ResponseEntity.ok(profileService.getMemberProfile(memberId));
    }

    @Operation(summary = "타 유저 플레이리스트 목록 조회", description = "타 유저 페이지에서 플레이리스트 목록 조회")
    @GetMapping("/{memberId}/playlists")
    public ResponseEntity<?> getAllPlaylists(@PathVariable("memberId") final Long memberId){
        return ResponseEntity.ok(playlistService.getAllPlaylists(memberId));
    }
}
