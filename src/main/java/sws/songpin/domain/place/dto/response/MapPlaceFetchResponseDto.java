package sws.songpin.domain.place.dto.response;

import org.springframework.data.domain.Slice;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record MapPlaceFetchResponseDto(
        int mapPlaceCount,
        Set<MapPlaceUnitDto> mapPlaceSet
){
    public static MapPlaceFetchResponseDto from(Slice<MapPlaceProjectionDto> dtoSlice) {
        // Collecting entries by placeId
        Map<Long, MapPlaceProjectionDto> latestPlaceMap = dtoSlice.stream()
                .collect(Collectors.toMap(
                        MapPlaceProjectionDto::getPlaceId,
                        dto -> dto,
                        (existing, replacement) -> {
                            if (existing.getListenedDate().isAfter(replacement.getListenedDate())) {
                                return existing;
                            } else {
                                return replacement;
                            }
                        }
                ));
        Set<MapPlaceUnitDto> mapPlaceSet = latestPlaceMap.values().stream()
                .map(MapPlaceUnitDto::from)
                .collect(Collectors.toCollection(HashSet::new));
        return new MapPlaceFetchResponseDto(
                mapPlaceSet.size(),
                mapPlaceSet
        );
    }
}
