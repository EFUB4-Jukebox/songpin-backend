package sws.songpin.domain.pin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.entity.Visibility;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.service.SongService;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.service.GenreService;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.service.PlaceService;

@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;
    private final MemberService memberService;
    private final SongService songService;
    private final PlaceService placeService;
    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<Pin> createPin(@RequestBody PinRequestDto request) {
        Member member = memberService.getCurrentMember();
        Song song = songService.getSong(request.title(), request.artist(), request.imgPath())
                .orElseThrow(() -> new RuntimeException("Song not found"));
        Place place = placeService.getPlace(request.placeName())
                .orElseThrow(() -> new RuntimeException("Place not found"));
        Genre genre = genreService.getGenre(request.genreName())
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        Visibility visibility = request.toVisibility();

        Pin pin = pinService.createPin(
                request.listenedDate(),
                request.memo(),
                visibility,
                member,
                song,
                place,
                genre
        );

        return ResponseEntity.ok(pin);
    }
}
