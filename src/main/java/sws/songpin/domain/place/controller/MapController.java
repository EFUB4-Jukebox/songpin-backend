package sws.songpin.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.place.dto.request.MapBasicRequestDto;
import sws.songpin.domain.place.dto.request.MapCustomPeriodRequestDto;
import sws.songpin.domain.place.dto.request.MapRecentPeriodRequestDto;
import sws.songpin.domain.place.dto.response.MapResponseDto;
import sws.songpin.domain.place.service.MapService;

@Tag(name = "Map", description = "장소들을 지도에 마커로 표시하기 위해, 요청 조건을 충족하는 최대 100개 장소의 위도/경도 좌표를 반환하는 API입니다.")
@RestController
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @Operation(summary = "지도에 마커를 띄울 장소 좌표들 가져오기-기본", description = "요청한 좌표 영역 안에 위치한 장소 좌표들을 불러옵니다.")
    @GetMapping
    public ResponseEntity<?> getPlaceCoordinates(@RequestBody @Valid MapBasicRequestDto requestDto) {
        MapResponseDto responseDto = mapService.getPlacesWithinBoundsByBasic(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "지도에 마커를 띄울 장소 좌표들 가져오기-기간 선택", description = "요청한 좌표 영역 안에 위치하고, 선택한 기간 조건이 충족되는 장소 좌표들을 불러옵니다.")
    @GetMapping("/period/recent")
    public ResponseEntity<?> getRecentPeriodPlaceCoordinates(@RequestBody @Valid MapRecentPeriodRequestDto requestDto) {
        MapResponseDto responseDto = mapService.getPlaceCoordinatesByRecentPeriod(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "지도에 마커를 띄울 장소 좌표들 가져오기-기간 직접 설정", description = "요청한 좌표 영역 안에 위치하고, 직접 설정한 기간 조건이 충족되는 장소 좌표들을 불러옵니다.")
    @GetMapping("/period/custom")
    public ResponseEntity<?> getCustomPeriodPlaceCoordinates(@RequestBody @Valid MapCustomPeriodRequestDto requestDto) {
        MapResponseDto responseDto = mapService.getPlacesWithinBoundsByCustomPeriod(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}