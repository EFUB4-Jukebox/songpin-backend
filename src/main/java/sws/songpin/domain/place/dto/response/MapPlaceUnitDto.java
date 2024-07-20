package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.genre.entity.GenreName;

public record MapPlaceUnitDto(
        Long placeId,
        Double latitude,
        Double longitude,
        Long placePinCount,
        Long songId,
        GenreName genreName
){
    public static MapPlaceUnitDto from(MapPlaceProjectionDto dto) {
        return new MapPlaceUnitDto(dto.getPlaceId(), dto.getLatitude(), dto.getLongitude(), dto.getPlacePinCount(), dto.getSongId(), dto.getGenreName());
    }
}