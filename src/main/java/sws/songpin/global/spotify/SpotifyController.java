package sws.songpin.global.spotify;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyUtil spotifyUtil;

    @GetMapping("/external/songs")
    public ResponseEntity<List<Track>> searchTracks(@RequestParam("keyword") String keyword) {
        List<Track> tracks = spotifyUtil.searchTracks(keyword);
        return ResponseEntity.ok(tracks);
    }
}
