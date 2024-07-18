package sws.songpin.domain.pin.dto.response;

import java.util.List;

public record FeedPinListResponseDto(
        int feedPinCount,
        List<FeedPinUnitDto> feedPinList) {
}
