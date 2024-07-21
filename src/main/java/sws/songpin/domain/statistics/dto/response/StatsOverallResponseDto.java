package sws.songpin.domain.statistics.dto.response;

import sws.songpin.domain.genre.entity.GenreName;

public record StatsOverallResponseDto (
        long totalPinCount,
        StatsPopularSongDto popularSong,
        StatsPlaceUnitDto popularPlace,
        GenreName popularGenreName
) {
    public static StatsOverallResponseDto from(long totalPinCount, StatsPopularSongDto popularSong, StatsPlaceUnitDto popularPlace, GenreName popularGenreName) {
        return new StatsOverallResponseDto(
                totalPinCount,
                popularSong,
                popularPlace,
                popularGenreName
        );
    }
}