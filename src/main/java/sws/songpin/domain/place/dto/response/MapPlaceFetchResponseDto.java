package sws.songpin.domain.place.dto.response;

import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record MapPlaceFetchResponseDto(
        Long mapPlaceCount,
        Set<MapPlaceUnitDto> mapPlaceSet
){
    public static MapPlaceFetchResponseDto from(Page<MapPlaceProjectionDto> dtoPage) {
        Set<MapPlaceUnitDto> mapPlaceSet = dtoPage.stream()
                .map(MapPlaceUnitDto::from)
                .collect(Collectors.toCollection(HashSet::new));
        return new MapPlaceFetchResponseDto(
                dtoPage.getTotalElements(),
                mapPlaceSet
        );
    }
}
