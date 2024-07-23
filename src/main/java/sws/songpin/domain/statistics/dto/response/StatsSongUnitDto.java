package sws.songpin.domain.statistics.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.statistics.dto.projection.StatsSongProjectionDto;

public record StatsSongUnitDto(
        GenreName genreName,
        String title,
        String artist,
        String imgPath
){
    public static StatsSongUnitDto from(StatsSongProjectionDto dto) {
        return new StatsSongUnitDto(dto.avgGenreName(), dto.title(), dto.artist(), dto.imgPath());
    }
}