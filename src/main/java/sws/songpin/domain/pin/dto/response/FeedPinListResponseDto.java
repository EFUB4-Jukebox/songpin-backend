package sws.songpin.domain.pin.dto.response;

import java.util.List;

public record FeedPinListResponseDto(
        List<FeedPinUnitDto> feedPinList,
        int feedPinCount) {
}
