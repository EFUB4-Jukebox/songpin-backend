package sws.songpin.domain.pin.dto.response;

import java.util.List;

public record PinBasicListResponseDto(
        List<PinBasicUnitDto> pinList
) {
}
