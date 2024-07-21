package sws.songpin.domain.statistics.dto.response;

public record StatsOverallResponseDto (
        Long totalPin,
        StatsSongUnitDto popularSong,
        StatsPlaceUnitDto popularPlace,
        StatsPlaceUnitDto popularGenre
) {
}
