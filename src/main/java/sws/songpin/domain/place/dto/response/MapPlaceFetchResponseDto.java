package sws.songpin.domain.place.dto.response;

import org.springframework.data.domain.Slice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record MapPlaceFetchResponseDto(
        int mapPlaceCount,
        Set<MapPlaceUnitDto> mapPlaceSet
){
    public static MapPlaceFetchResponseDto from(Slice<MapPlaceProjectionDto> dtoSlice) {
        Set<MapPlaceUnitDto> mapPlaceSet = dtoSlice.stream()
                .map(MapPlaceUnitDto::from)
                .collect(Collectors.toCollection(HashSet::new));
        return new MapPlaceFetchResponseDto(
                mapPlaceSet.size(),
                mapPlaceSet
        );
    }
}
