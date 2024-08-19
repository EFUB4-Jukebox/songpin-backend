package sws.songpin.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.place.dto.request.MapFetchEntirePeriodRequestDto;
import sws.songpin.domain.place.dto.request.MapFetchCustomPeriodRequestDto;
import sws.songpin.domain.place.dto.request.MapFetchRecentPeriodRequestDto;
import sws.songpin.domain.place.dto.response.MapPlaceFetchResponseDto;
import sws.songpin.domain.place.service.MapService;

@Tag(name = "Map", description = "장소들을 지도에 마커로 표시하기 위해, 요청 조건을 충족하는 최대 100개 장소의 위도/경도 좌표를 반환하는 API입니다.")
@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @Operation(summary = "장소 좌표들 가져오기-전체 기간", description = "전체 기간 범위에서 핀이 1개 이상 등록된, 요청 좌표 영역 안의 장소 좌표들을 불러옵니다.")
    @PostMapping
    public ResponseEntity<?> getMapPlacesWithinBounds(@RequestBody @Valid MapFetchEntirePeriodRequestDto requestDto) {
        MapPlaceFetchResponseDto responseDto = mapService.getMapPlacesWithinBoundsByEntirePeriod(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "장소 좌표들 가져오기-최근 기준 기간", description = "선택한 기간 조건 안에 핀이 1개 이상 등록된, 요청 좌표 영역 안의 장소 좌표들을 불러옵니다.")
    @PostMapping("/period/recent")
    public ResponseEntity<?> getMapPlacesWithinBoundsForRecentPeriod(@RequestBody @Valid MapFetchRecentPeriodRequestDto requestDto) {
        MapPlaceFetchResponseDto responseDto = mapService.getMapPlacesWithinBoundsByRecentPeriod(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "장소 좌표들 가져오기-기간 직접 설정", description = "직접 설정한 기간 조건 안에 핀이 1개 이상 등록된, 요청 좌표 영역 안의 장소 좌표들을 불러옵니다.")
    @PostMapping("/period/custom")
    public ResponseEntity<?> getMapPlacesWithinBoundsForCustomPeriod(@RequestBody @Valid MapFetchCustomPeriodRequestDto requestDto) {
        MapPlaceFetchResponseDto responseDto = mapService.getMapPlacesWithinBoundsByCustomPeriod(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "유저가 핀을 등록한 장소 좌표들 가져오기", description = "유저가 핀을 등록한 장소 좌표들을 가져옵니다.")
    @GetMapping("/members/{handle}")
    public ResponseEntity<?> getMapPlacesOfMember(@PathVariable final String handle) {
        MapPlaceFetchResponseDto responseDto = mapService.getMapPlacesOfMember(handle);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "플레이리스트의 핀들 장소 좌표들 가져오기", description = "플레이리스트의 핀들 장소 좌표들을 가져옵니다.")
    @GetMapping("/playlists/{playlistId}")
    public ResponseEntity<?> getMapPlacesOfPlaylist(@PathVariable final Long playlistId) {
        MapPlaceFetchResponseDto responseDto = mapService.getMapPlacesOfPlaylist(playlistId);
        return ResponseEntity.ok(responseDto);
    }
}