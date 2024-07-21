package sws.songpin.domain.statistics.dto.response;

import sws.songpin.domain.genre.entity.GenreName;

public record StatsSongUnitDto(
        String title,
        String artist,
        String imgPath,
        GenreName avgGenreName
){
    public static StatsSongUnitDto from(StatsSongProjectionDto dto) {
        return new StatsSongUnitDto(dto.getTitle(), dto.getArtist(), dto.getImgPath(), dto.getAvgGenreName());
    }
}