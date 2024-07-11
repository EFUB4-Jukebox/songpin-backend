package sws.songpin.domain.song.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.song.dto.response.SongDetailResponseDto;
import sws.songpin.domain.song.service.SongService;

@Tag(name = "Song", description = "Song 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @GetMapping("/songs/{songId}")
    @Operation(summary = "음악 상세정보 조회", description = "음악 상세정보를 조회합니다.")
    public ResponseEntity<SongDetailResponseDto> getSongDetail(@Valid @PathVariable Long songId) {
        SongDetailResponseDto songDetail = songService.getSongDetail(songId);
        return ResponseEntity.ok(songDetail);
    }
}
