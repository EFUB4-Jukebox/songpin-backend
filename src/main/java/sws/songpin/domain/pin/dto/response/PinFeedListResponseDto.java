package sws.songpin.domain.pin.dto.response;

import java.util.List;

public record PinFeedListResponseDto(
        int pinFeedCount,
        List<PinFeedUnitDto> pinFeedList) {
}
