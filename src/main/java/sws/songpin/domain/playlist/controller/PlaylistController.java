package sws.songpin.domain.playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.pin.domain.Pin;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.playlist.dto.request.PlaylistRequestDto;
import sws.songpin.domain.playlist.dto.request.PlaylistUpdateRequestDto;
import sws.songpin.domain.playlist.dto.request.PlaylistPinRequestDto;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlist.service.PlaylistService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PinService pinService;

    @Operation(summary = "플레이리스트 생성", description = "로그인한 사용자의 새로운 플레이리스트 생성")
    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody @Valid PlaylistRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(playlistService.createPlaylist(requestDto));
    }

    @Operation(summary = "플레이리스트에 핀 추가", description = "플레이리스트에 특정 핀 추가")
    @PostMapping("/pins")
    public ResponseEntity<?> addPlaylistPin(@RequestBody @Valid PlaylistPinRequestDto requestDto){
        Playlist playlist = playlistService.findPlaylistById(requestDto.playlistId());
        Optional<Pin> pin = pinService.getPin(requestDto.pinId());
        playlistService.addPlaylistPin(playlist, pin.orElse(null));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "플레이리스트 상세정보", description = "특정 플레이리스트의 상세정보 보기")
    @GetMapping("/{playlistId}")
    public ResponseEntity<?> getPlaylist(@PathVariable("playlistId") Long playlistId){
        return ResponseEntity.ok(playlistService.getPlaylist(playlistId));
    }

    @Operation(summary = "플레이리스트 편집", description = "플레이리스트 이름, 공개여부, 순서 편집")
    @PatchMapping("/{playlistId}")
    public ResponseEntity<?> updatePlaylist(@PathVariable("playlistId") Long playlistId,
                                                          @RequestBody @Valid PlaylistUpdateRequestDto requestDto){
        playlistService.updatePlaylist(playlistId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "플레이리스트 삭제", description = "플레이리스트를 삭제")
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable("playlistId") Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.ok().build();
    }


}
