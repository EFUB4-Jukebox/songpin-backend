package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.place.entity.Place;

public record PlaceUnitDto(
        Long placeId,
        String placeName,
        int pinCount
) {
    public static PlaceUnitDto from(Place place, int pinCount) {
        return new PlaceUnitDto(
                place.getPlaceId(),
                place.getPlaceName(),
                pinCount
        );
    }
}