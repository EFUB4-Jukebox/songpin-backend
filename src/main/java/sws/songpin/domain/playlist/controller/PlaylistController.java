package sws.songpin.domain.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.playlist.dto.request.PlaylistAddRequestDto;
import sws.songpin.domain.playlist.dto.request.PlaylistUpdateRequestDto;
import sws.songpin.domain.playlist.dto.request.PlaylistPinAddRequestDto;
import sws.songpin.domain.playlist.service.PlaylistService;
import sws.songpin.domain.playlistpin.service.PlaylistPinService;

@Tag(name = "Playlist", description = "Playlist 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PlaylistPinService playlistPinService;

    @Operation(summary = "플레이리스트 생성", description = "로그인한 사용자의 새로운 플레이리스트 생성")
    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody @Valid PlaylistAddRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(playlistService.createPlaylist(requestDto));
    }

    @Operation(summary = "플레이리스트에 핀 추가", description = "플레이리스트에 특정 핀 추가")
    @PostMapping("/pins")
    public ResponseEntity<?> addPlaylistPin(@RequestBody @Valid PlaylistPinAddRequestDto requestDto) {
        playlistPinService.addPlaylistPin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "플레이리스트 상세정보", description = "특정 플레이리스트의 상세정보 보기")
    @GetMapping("/{playlistId}")
    public ResponseEntity<?> getPlaylist(@PathVariable("playlistId") final Long playlistId){
        return ResponseEntity.ok(playlistService.getPlaylist(playlistId));
    }

    @Operation(summary = "플레이리스트 편집", description = "플레이리스트 이름, 공개여부, 순서 편집")
    @PatchMapping("/{playlistId}")
    public ResponseEntity<?> updatePlaylist(@PathVariable("playlistId") final Long playlistId,
                                                          @RequestBody @Valid PlaylistUpdateRequestDto requestDto){
        playlistService.updatePlaylist(playlistId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "플레이리스트 삭제", description = "플레이리스트를 삭제")
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable("playlistId") final Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "플레이리스트 메인", description = "플레이리스트 메인 페이지 조회")
    @GetMapping("/main")
    public ResponseEntity<?> getPlaylistMain() {
        return ResponseEntity.ok(playlistService.getPlaylistMain());
    }

    @Operation(summary = "플레이리스트 검색", description = "플레이리스트 검색 결과를 선택한 정렬 기준에 따라 페이징으로 불러옵니다.")
    @GetMapping
    public ResponseEntity<?> playlistSearch(@RequestParam final String keyword,
                                         @RequestParam(defaultValue = "ACCURACY") final String sortBy,
                                         @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(playlistService.searchPlaylists(keyword, SortBy.from(sortBy), pageable));
    }


}
