package sws.songpin.domain.pin.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PinFeedListResponseDto(
        int totalPages,
        int currentPage,
        int pageSize,
        long totalElements,
        List<PinFeedUnitDto> pinFeedList) {
    public static PinFeedListResponseDto from(Page<PinFeedUnitDto> pinFeedPage) {
        return new PinFeedListResponseDto(
                pinFeedPage.getTotalPages(),
                pinFeedPage.getNumber(),
                pinFeedPage.getSize(),
                pinFeedPage.getTotalElements(),
                pinFeedPage.getContent()
        );
    }
}
