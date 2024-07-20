package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.genre.entity.GenreName;

public record MapFetchUnitDto ( // JPA DTO 직접 조회 위해 class로 생성
        Long placeId,
        Double latitude,
        Double longitude,
        Long placePinCount,
        Long songId,
        GenreName genreName
){
    public static MapFetchUnitDto from(MapPlaceProjectionDto dto) {
        return new MapFetchUnitDto(dto.getPlaceId(), dto.getLatitude(), dto.getLongitude(), dto.getPlacePinCount(), dto.getSongId(), dto.getGenreName());
    }
}