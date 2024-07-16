package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.place.entity.Place;

import java.util.List;

public record MapFetchResponseDto(
        int placeCount,
        List<MapPlaceDto> placeList
) {
    public static MapFetchResponseDto from(List<Place> places) {
        int placeCount = places.size();
        List<MapPlaceDto> placeDtoList = places.stream()
                .map(MapPlaceDto::from)
                .toList();
        return new MapFetchResponseDto(placeCount, placeDtoList);
    }

}