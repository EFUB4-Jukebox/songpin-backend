package sws.songpin.domain.pin.dto.response;

import java.util.List;

public record PinBasicListDto(
        List<PinBasicUnitDto> pinList,
        int basicCount
) {
}
