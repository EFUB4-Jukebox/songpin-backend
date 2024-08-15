package sws.songpin.domain.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.place.service.PlaceService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Tag(name = "Place", description = "Place 관련 API입니다.")
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @Operation(summary = "장소 상세정보", description = "장소 상세정보를 불러옵니다.")
    @GetMapping("/{placeId}")
    public ResponseEntity<?> placeDetails(@PathVariable final Long placeId) {
        return ResponseEntity.ok().body(placeService.getPlaceDetails(placeId));
    }

    @Operation(summary = "장소 검색", description = "장소 검색 결과를 선택한 정렬 기준에 따라 페이징으로 불러옵니다.")
    @GetMapping
    public ResponseEntity<?> placeSearch(@RequestParam final String keyword,
                                         @RequestParam(defaultValue = "ACCURACY") final String sortBy,
                                         @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(placeService.searchPlaces(keyword, SortBy.from(sortBy), pageable));
    }
}
