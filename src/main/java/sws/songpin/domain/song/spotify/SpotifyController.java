package sws.songpin.domain.song.spotify;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.song.service.SongService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotifyController {

    private final SongService songService;
//    private static final Logger logger = LoggerFactory.getLogger(SpotifyController.class);

    @GetMapping("/external/songs")
    public List<PinRequestDto.SongRequestDto> searchTracks(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return songService.searchTracks(keyword, offset);
    }
}
