package sws.songpin.domain.statistics.dto.response;

import sws.songpin.domain.statistics.dto.projection.StatsSongProjectionDto;

public record StatsPopularSongDto(
        String title,
        String artist,
        String imgPath
) {
    public static StatsPopularSongDto from(StatsSongProjectionDto dto) {
        return new StatsPopularSongDto(dto.title(), dto.artist(), dto.imgPath());
    }
}