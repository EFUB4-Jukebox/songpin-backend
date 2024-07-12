package sws.songpin.domain.song.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.song.dto.response.SongDetailsPinDto;
import sws.songpin.domain.song.dto.response.SongDetailsResponseDto;
import sws.songpin.domain.song.service.SongService;

import java.util.List;

@Tag(name = "Song", description = "Song 관련 API입니다.")
@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;
    private final PinService pinService;

    @GetMapping("/{songId}")
    @Operation(summary = "노래 상세보기", description = "노래의 상세 정보를 조회합니다.")
    public ResponseEntity<SongDetailsResponseDto> getSongDetails(@PathVariable("songId") final Long songId) {
        SongDetailsResponseDto songDetails = songService.getSongDetails(songId);
        return ResponseEntity.ok(songDetails);
    }

    @GetMapping("/{songId}/pins")
    @Operation(summary = "노래에 대한 모든 핀", description = "해당 노래에 대한 모든 핀을 조회합니다.")
    public ResponseEntity<List<SongDetailsPinDto>> getPinsForSong(@PathVariable("songId") final Long songId) {
        List<SongDetailsPinDto> allPins = pinService.getPinsForSong(songId, false);
        return ResponseEntity.ok(allPins);
    }

    @GetMapping("/{songId}/pins/me")
    @Operation(summary = "현재 로그인한 사용자의 핀 보기", description = "현재 로그인한 사용자가 만든 핀만 조회합니다.")
    public ResponseEntity<List<SongDetailsPinDto>> getMyPinsForSong(@PathVariable("songId") final Long songId) {
        List<SongDetailsPinDto> myPins = pinService.getPinsForSong(songId, true);
        return ResponseEntity.ok(myPins);
    }
}
