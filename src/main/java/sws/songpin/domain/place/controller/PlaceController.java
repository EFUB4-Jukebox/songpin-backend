package sws.songpin.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.place.service.PlaceService;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ResponseEntity<?> getPlaceDetails(@PathVariable final Long placeId) {
        return ResponseEntity.ok().body(placeService.getPlaceDetails(placeId));
    }

    @GetMapping("/")
    public ResponseEntity<?> searchPlaces(@RequestParam final String keyword,
                                          @RequestParam(required = false, defaultValue = "ACCURACY") final SortBy sortBy,
                                          @RequestParam(required = false, defaultValue = "0") final int offset) { // offset 자료형 확인 필요
        return ResponseEntity.ok().body(placeService.searchPlaces(keyword, sortBy, offset));
    }
}
