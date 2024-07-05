package sws.songpin.domain.pin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.entity.Visibility;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.service.SongService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final SongRepository songRepository;
    private final SongService songService;

    public Pin createPin(LocalDate listenedDate, String memo, Visibility visibility, Member member, Song song, Place place, Genre genre) {
        Pin pin = Pin.builder()
                .listenedDate(listenedDate)
                .memo(memo)
                .visibility(visibility)
                .member(member)
                .song(song)
                .place(place)
                .genre(genre)
                .build();

        pin = pinRepository.save(pin);
        updateSongAvgGenre(song);

        return pin;
    }

    private void updateSongAvgGenre(Song song) {
        List<Genre> genres = pinRepository.findAllBySong(song).stream()
                .map(Pin::getGenre)
                .collect(Collectors.toList());

        GenreName mostCommonGenre = songService.findMostCommonGenre(genres);
        song.setAvgGenreName(mostCommonGenre);
        songRepository.save(song);
    }

    // 임시 개발용
    public Optional<Pin> getPin(Long id) {
        return pinRepository.findById(id);
    }

}