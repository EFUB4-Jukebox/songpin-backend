package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.place.entity.Place;

import java.util.List;

public record MapResponseDto(
        int placeCount,
        List<MapPlaceDto> placeList
) {
    public static MapResponseDto from(List<Place> places) {
        int placeCount = places.size();
        List<MapPlaceDto> placeDtoList = places.stream()
                .map(MapPlaceDto::from)
                .toList();
        return new MapResponseDto(placeCount, placeDtoList);
    }

}