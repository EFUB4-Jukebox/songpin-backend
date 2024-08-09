package sws.songpin.domain.song.spotify;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.song.dto.response.SpotifySearchDto;
import sws.songpin.domain.song.service.SongService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotifyController {

    private final SongService songService;

    @GetMapping("/external/songs")
    public List<SpotifySearchDto> searchTracks(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset) throws UnsupportedEncodingException {
        String decodedKeyword = URLDecoder.decode(keyword, "UTF-8");
        return songService.searchTracks(decodedKeyword, offset);
    }
}
