package sws.songpin.domain.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.playlist.service.PlaylistService;

@RestController
@RequiredArgsConstructor
public class UserPlaylistController {
    private final PlaylistService playlistService;
    private final MemberService memberService;

    @Operation(summary = "내 플레이리스트 목록 조회", description = "마이페이지에서 내 플레이리스트 목록 조회")
    @GetMapping("/me/playlists")
    public ResponseEntity<?> getAllPlaylists(){
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @Operation(summary = "타 유저 플레이리스트 목록 조회", description = "타 유저 페이지에서 플레이리스트 목록 조회")
    @GetMapping("/members/{memberId}/playlists")
    public ResponseEntity<?> getAllPlaylists(@PathVariable("memberId") final Long memberId){
        return ResponseEntity.ok(playlistService.getAllPlaylists(memberId));
    }

    @Operation(summary = "내 북마크 목록 조회", description = "마이페이지에서 북마크 목록 조회")
    @GetMapping("/me/bookmarks")
    public ResponseEntity<?> getAllBookmarks(){
        return ResponseEntity.ok(playlistService.getAllBookmarks());
    }
}
