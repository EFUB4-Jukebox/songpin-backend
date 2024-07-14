package sws.songpin.domain.place.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PlaceSearchResponseDto(
        int currentPage,
        int pageSize, // 현재 페이지 크기 (명확화 위해)
        long totalElements,
        int totalPages,
        List<PlaceUnitDto> placeList
) {
    public static PlaceSearchResponseDto from(Page<PlaceUnitDto> placePage) {
        return new PlaceSearchResponseDto(
                placePage.getNumber(),
                placePage.getSize(),
                placePage.getTotalElements(),
                placePage.getTotalPages(),
                placePage.getContent()
        );
    }
}