package sws.songpin.domain.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.place.repository.MapPlaceRepository;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.statistics.dto.response.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true) // Transaction 모두 읽기 전용
@RequiredArgsConstructor
public class StatisticsService {
    private final MapPlaceRepository mapPlaceRepository;
    private final SongRepository songRepository;

    public StatsOverallResponseDto getOverallStats() {
        // todo: totalPin 로직 작성
        Pageable pageable = PageRequest.of(0, 1);
        Slice<StatsMapPlaceProjectionDto> placeProjectionDtos = mapPlaceRepository.findTopPlaces(pageable);
        StatsPlaceUnitDto popularPlace = StatsPlaceUnitDto.from(placeProjectionDtos.getContent().get(0));
        Slice<StatsSongProjectionDto> songProjectionDtoSlice = songRepository.findTopSongs(pageable);
        StatsSongUnitDto popularSong = StatsSongUnitDto.from(songProjectionDtoSlice.getContent().get(0));
        // todo: statsGenre 로직 작성
    }

    public StatsGenreResponseDto getTopPlacesAndSongsFromAllGenres() {
        GenreName[] genreNames = GenreName.values();
        Pageable pageable = PageRequest.of(0, 1);
        List<StatsPlaceUnitDto> placeUnitDtos = getTopPlacesFromAllGenres(genreNames, pageable);
        List<StatsSongUnitDto> songUnitDtos = getTopSongsFromAllGenres(genreNames, pageable);
        return StatsGenreResponseDto.from(placeUnitDtos, songUnitDtos);
    }

    private List<StatsPlaceUnitDto> getTopPlacesFromAllGenres(GenreName[] genreNames, Pageable pageable) {
        List<StatsPlaceUnitDto> placeUnitDtos = new ArrayList<>();
        for (GenreName genreName : genreNames) {
            Slice<StatsMapPlaceProjectionDto> dtoSlice = mapPlaceRepository.findTopPlacesByGenreName(genreName, pageable);
            if (!dtoSlice.isEmpty()) {
                placeUnitDtos.add(StatsPlaceUnitDto.from(dtoSlice.getContent().get(0)));
            }
        }
        return placeUnitDtos;
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
}
