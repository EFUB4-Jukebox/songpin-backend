package sws.songpin.domain.statistics.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.statistics.dto.projection.StatsPlaceProjectionDto;

public record StatsPlaceUnitDto(
        Long placeId,
        String placeName,
        Double latitude,
        Double longitude,
        Long placePinCount,
        GenreName genreName
){
    public static StatsPlaceUnitDto from(StatsPlaceProjectionDto dto) {
        return new StatsPlaceUnitDto(dto.getPlaceId(), dto.getPlaceName(), dto.getLatitude(), dto.getLongitude(), dto.getPlacePinCount(), dto.getGenreName());
    }
}