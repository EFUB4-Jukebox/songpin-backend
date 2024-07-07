package sws.songpin.domain.pin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.service.PlaceService;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.service.SongService;
import sws.songpin.domain.genre.service.GenreService;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final SongRepository songRepository;
    private final MemberService memberService;
    private final SongService songService;
    private final PlaceService placeService;
    private final GenreService genreService;

    public Pin createPin(PinRequestDto pinRequestDto) {
        Member member = memberService.getCurrentMember()
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Optional<Song> song = songService.getSongByProviderTrackCode(pinRequestDto.song().providerTrackCode());
        Optional<Place> place = placeService.getPlaceByProviderAddressId(pinRequestDto.place().providerAddressId());
        Genre genre = genreService.getGenreByName(pinRequestDto.genreName())
                .orElseThrow(() -> new CustomException(ErrorCode.GENRE_NOT_FOUND));

        if (pinRepository.existsByMemberAndSongAndPlaceAndListenedDate(member, song.orElse(null), place.orElse(null), pinRequestDto.listenedDate())) {
            throw new CustomException(ErrorCode.PIN_ALREADY_EXISTS);
        }

        Place finalPlace;
        if (place.isEmpty()) {
            PinRequestDto.PlaceRequestDto placeRequest = pinRequestDto.place();
            finalPlace = Place.builder()
                    .placeName(placeRequest.placeName())
                    .address(placeRequest.address())
                    .providerAddressId(placeRequest.providerAddressId())
                    .latitude(placeRequest.latitude())
                    .longitude(placeRequest.longitude())
                    .build();
            finalPlace = placeService.createPlace(finalPlace);
        } else {
            finalPlace = place.get();
        }

        Song finalSong;
        if (song.isEmpty()) {
            PinRequestDto.SongRequestDto songRequest = pinRequestDto.song();
            finalSong = Song.builder()
                    .providerTrackCode(songRequest.providerTrackCode())
                    .title(songRequest.title())
                    .artist(songRequest.artist())
                    .imgPath(songRequest.imgPath())
                    .build();
            finalSong = songService.createSong(finalSong);
        } else {
            finalSong = song.get();
        }

        Pin pin = Pin.builder()
                .listenedDate(pinRequestDto.listenedDate())
                .memo(pinRequestDto.memo())
                .visibility(pinRequestDto.visibility())
                .member(member)
                .song(finalSong)
                .place(finalPlace)
                .genre(genre)
                .build();

        pin = pinRepository.save(pin);
        updateSongAvgGenreName(finalSong);

        return pin;
    }

    private void updateSongAvgGenreName(Song song) {
        List<Genre> genres = pinRepository.findAllBySong(song).stream()
                .map(Pin::getGenre)
                .collect(Collectors.toList());

        Optional<GenreName> avgGenreName = songService.findAvgGenreName(genres);
        avgGenreName.ifPresent(song::setAvgGenreName);
        songRepository.save(song);
    }

    public Optional<Pin> getPinById(Long id) {
        return pinRepository.findById(id);
    }
}
