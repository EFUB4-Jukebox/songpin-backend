package sws.songpin.domain.member.dto.response;

import org.springframework.data.domain.Page;
import sws.songpin.domain.pin.dto.response.PinBasicUnitDto;

import java.util.List;

public record MyPinSearchResponseDto(
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        List<PinBasicUnitDto> myPinList
) {
    public static MyPinSearchResponseDto from(Page<PinBasicUnitDto> page) {
        return new MyPinSearchResponseDto(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getContent()
        );
    }
}