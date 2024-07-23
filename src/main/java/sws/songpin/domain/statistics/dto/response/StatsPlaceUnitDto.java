package sws.songpin.domain.statistics.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.statistics.dto.projection.StatsPlaceProjectionDto;

public record StatsPlaceUnitDto(
        GenreName genreName,
        Long placeId,
        String placeName,
        Double latitude,
        Double longitude,
        Long placePinCount
){
    public static StatsPlaceUnitDto from(StatsPlaceProjectionDto dto) {
        return new StatsPlaceUnitDto(dto.genreName(), dto.placeId(), dto.placeName(), dto.latitude(), dto.longitude(), dto.placePinCount());
    }
}