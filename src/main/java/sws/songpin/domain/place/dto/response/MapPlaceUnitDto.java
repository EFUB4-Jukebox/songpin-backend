package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.place.dto.projection.MapPlaceProjectionDto;

public record MapPlaceUnitDto(
        Long placeId,
        Double latitude,
        Double longitude,
        Long placePinCount,
        Long latestSongId,
        GenreName latestGenreName
){
    public static MapPlaceUnitDto from(MapPlaceProjectionDto dto) {
        return new MapPlaceUnitDto(dto.placeId(), dto.latitude(), dto.longitude(), dto.placePinCount(), dto.songId(), dto.genreName());
    }
}