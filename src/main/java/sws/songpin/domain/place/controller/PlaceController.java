package sws.songpin.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.place.service.PlaceService;

@Tag(name = "Place", description = "Place 관련 API입니다.")
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @Operation(summary = "장소 상세정보", description = "장소 상세정보를 불러옵니다.")
    @GetMapping("/{placeId}")
    public ResponseEntity<?> getPlaceDetails(@PathVariable final Long placeId) {
        return ResponseEntity.ok().body(placeService.getPlaceDetails(placeId));
    }

    @Operation(summary = "장소 검색", description = "장소 검색 결과를 선택한 정렬 기준에 따라 페이징으로 불러옵니다.")
    @GetMapping
    public ResponseEntity<?> searchPlaces(@RequestParam final String keyword,
                                          @RequestParam(required = false, defaultValue = "ACCURACY") final SortBy sortBy,
                                          @RequestParam final int offset) { // offset 자료형 확인 필요
        return ResponseEntity.ok().body(placeService.searchPlaces(keyword, sortBy, offset));
    }
}
