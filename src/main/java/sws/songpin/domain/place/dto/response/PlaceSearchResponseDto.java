package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.place.entity.Place;

import java.util.List;
import java.util.stream.Collectors;

public record PlaceSearchResponseDto(
        int placeCount,
        List<PlaceUnitDto> placeList
) {
    public static PlaceSearchResponseDto from(int placeCount, List<Place> placeList) {
        List<PlaceUnitDto> placeUnitDtos = placeList.stream()
                .map(p -> PlaceUnitDto.from(p, p.getPins().size()))
                .collect(Collectors.toList());
        return new PlaceSearchResponseDto(placeCount, placeUnitDtos);
    }
}