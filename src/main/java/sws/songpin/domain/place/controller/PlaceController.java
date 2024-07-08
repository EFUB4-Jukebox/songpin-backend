package sws.songpin.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.place.service.PlaceService;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/")
    public ResponseEntity<?> searchPlaces(@RequestParam final String keyword,
                                          @RequestParam final SortBy sortBy) {

    }
}
