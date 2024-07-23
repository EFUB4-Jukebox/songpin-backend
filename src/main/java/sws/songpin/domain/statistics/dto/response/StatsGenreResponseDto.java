package sws.songpin.domain.statistics.dto.response;

import java.util.List;

public record StatsGenreResponseDto(
    List<StatsPlaceUnitDto> placeList,
    List<StatsSongUnitDto> songList
) {
    public static StatsGenreResponseDto from(List<StatsPlaceUnitDto> placeUnitDtos, List<StatsSongUnitDto> songUnitDtos) {
        return new StatsGenreResponseDto(placeUnitDtos, songUnitDtos);
    }
}
