package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.place.entity.Place;

public record PlaceUnitDto(
        Long placeId,
        String placeName,
        int placePinCount
) {
    public static PlaceUnitDto from(Place place, int placePinCount) {
        return new PlaceUnitDto(
                place.getPlaceId(),
                place.getPlaceName(),
                placePinCount
        );
    }
}