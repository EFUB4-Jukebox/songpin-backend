package sws.songpin.domain.song.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.pin.service.PinService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import sws.songpin.domain.song.dto.response.SongDetailsPinListResponseDto;
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
    public ResponseEntity<?> getSongDetails(@PathVariable("songId") final Long songId) {
        SongDetailsResponseDto songDetails = songService.getSongDetails(songId);
        return ResponseEntity.ok(songDetails);
    }

    @GetMapping("/{songId}/pins")
    @Operation(summary = "노래에 대한 모든 핀", description = "해당 노래에 대한 모든 핀을 조회합니다.")
    public ResponseEntity<?> getPinsForSong(@PathVariable("songId") final Long songId, Pageable pageable) {
        SongDetailsPinListResponseDto allPins = pinService.getPinsForSong(songId, false, pageable);
        return ResponseEntity.ok(allPins);
    }

    @GetMapping("/{songId}/pins/me")
    @Operation(summary = "현재 로그인한 사용자의 핀 보기", description = "현재 로그인한 사용자가 만든 핀만 조회합니다.")
    public ResponseEntity<?> getMyPinsForSong(@PathVariable("songId") final Long songId, Pageable pageable) {
        SongDetailsPinListResponseDto myPins = pinService.getPinsForSong(songId, true, pageable);
        return ResponseEntity.ok(myPins);
    }

    @GetMapping
    @Operation(summary = "노래 검색", description = "노래 검색 결과를 선택한 정렬 기준에 따라 페이징으로 불러옵니다.")
    public ResponseEntity<?> songSearch(@RequestParam("keyword") final String keyword,
                                        @RequestParam(value = "sortBy", defaultValue = "ACCURACY") final String sortBy,
                                        @PageableDefault(size = 20) final Pageable pageable){
        return ResponseEntity.ok().body(songService.searchSongs(keyword, SortBy.from(sortBy), pageable));
    }
}
