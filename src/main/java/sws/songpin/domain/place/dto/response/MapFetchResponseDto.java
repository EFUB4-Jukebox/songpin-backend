package sws.songpin.domain.place.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record MapFetchResponseDto(
        Long mapPlaceCount,
        List<MapPlaceUnitDto> mapPlaceList
){
    public static MapFetchResponseDto from(Page<MapPlaceUnitDto> pages) {
        return new MapFetchResponseDto(
                pages.getTotalElements(),
                pages.getContent()
        );
    }
}
