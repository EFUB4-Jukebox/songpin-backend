package sws.songpin.domain.place.dto.response;

import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record MapFetchResponseDto(
        Long mapPlaceCount,
        Set<MapFetchUnitDto> mapPlaceSet
){
    public static MapFetchResponseDto from(Page<MapPlaceProjectionDto> dtoPage) {
        Set<MapFetchUnitDto> mapPlaceSet = dtoPage.stream()
                .map(MapFetchUnitDto::from)
                .collect(Collectors.toCollection(HashSet::new));
        return new MapFetchResponseDto(
                dtoPage.getTotalElements(),
                mapPlaceSet
        );
    }
}
