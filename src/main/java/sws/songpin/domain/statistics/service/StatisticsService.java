package sws.songpin.domain.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.repository.MapPlaceRepository;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.statistics.dto.response.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Transaction 모두 읽기 전용
@RequiredArgsConstructor
public class StatisticsService {
    private final MapPlaceRepository mapPlaceRepository;
    private final SongRepository songRepository;
    private final PinRepository pinRepository;

    // 종합 통계

    public StatsOverallResponseDto getOverallStats() {
        int currentYear = LocalDate.now().getYear();
        long totalPinCount = pinRepository.countByListenedDateYear(currentYear);
        Pageable pageable = PageRequest.of(0, 1);
        StatsPopularSongDto popularSong = getMostPopularSongDto(pageable).orElse(null);
        StatsPlaceUnitDto popularPlace = getMostPopularPlaceDto(pageable).orElse(null);
        GenreName popularGenreName = getMostPopularGenreName().orElse(GenreName.POP);
        return StatsOverallResponseDto.from(totalPinCount, popularSong, popularPlace, popularGenreName);
    }

    private Optional<StatsPopularSongDto> getMostPopularSongDto(Pageable pageable) {
        Slice<StatsSongProjectionDto> topSongsSlice = songRepository.findTopSongs(pageable);
        if (topSongsSlice != null && !topSongsSlice.getContent().isEmpty()) {
            return Optional.of(StatsPopularSongDto.from(topSongsSlice.getContent().get(0)));
        }
        return Optional.empty();
    }

    private Optional<StatsPlaceUnitDto> getMostPopularPlaceDto(Pageable pageable) {
        Slice<StatsPlaceProjectionDto> topPlacesSlice = mapPlaceRepository.findTopPlaces(pageable);
        if (topPlacesSlice != null && !topPlacesSlice.getContent().isEmpty()) {
            return Optional.of(StatsPlaceUnitDto.from(topPlacesSlice.getContent().get(0)));
        }
        return Optional.empty();
    }

    private Optional<GenreName> getMostPopularGenreName() {
        List<Object[]> objectList = pinRepository.findMostPopularGenreName();
        if (!objectList.isEmpty()) {
            return Optional.of(GenreName.from(objectList.get(0)[0].toString()));
        } else {
            return Optional.empty();
        }
    }

    // 장르별 통계

    public StatsGenreResponseDto getPlaceAndSongStatsByGenre() {
        GenreName[] genreNames = GenreName.values();
        Pageable pageable = PageRequest.of(0, 1);
        List<StatsPlaceUnitDto> placeUnitDtos = getTopPlacesFromAllGenres(genreNames, pageable);
        List<StatsSongUnitDto> songUnitDtos = getTopSongsFromAllGenres(genreNames, pageable);
        return StatsGenreResponseDto.from(placeUnitDtos, songUnitDtos);
    }

    private List<StatsSongUnitDto> getTopSongsFromAllGenres(GenreName[] genreNames, Pageable pageable) {
        List<StatsSongUnitDto> songUnitDtos = new ArrayList<>();
        for (GenreName genreName : genreNames) {
            Slice<StatsSongProjectionDto> dtoSlice = songRepository.findTopSongsByGenreName(genreName, pageable);
            if (!dtoSlice.isEmpty()) {
                songUnitDtos.add(StatsSongUnitDto.from(dtoSlice.getContent().get(0)));
            }
        }
        return songUnitDtos;
    }

    private List<StatsPlaceUnitDto> getTopPlacesFromAllGenres(GenreName[] genreNames, Pageable pageable) {
        List<StatsPlaceUnitDto> placeUnitDtos = new ArrayList<>();
        for (GenreName genreName : genreNames) {
            Slice<StatsPlaceProjectionDto> dtoSlice = mapPlaceRepository.findTopPlacesByGenreName(genreName, pageable);
            if (!dtoSlice.isEmpty()) {
                placeUnitDtos.add(StatsPlaceUnitDto.from(dtoSlice.getContent().get(0)));
            }
        }
        return placeUnitDtos;
    }
}
