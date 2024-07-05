package sws.songpin.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sws.songpin.domain.place.service.PlaceService;

@Controller
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping(value = "/map", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> getKakaoApiFromAddress(@RequestParam("place") String roadFullAddr) {
        try {
            String jsonResponse = placeService.searchPlaceAsJson(roadFullAddr);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("PlaceError: " + e.getMessage());
        }
    }
}
