package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.entity.Visibility;

import java.time.LocalDate;

public record PinResponseDto(
        Long pinId,
        Long creatorId,
        String creatorNickname,
        LocalDate listenedDate,
        String memo,
        Visibility visibility,
        String placeName) {

    public static PinResponseDto from(Pin pin) {
        return new PinResponseDto(
                pin.getPinId(),
                pin.getMember().getMemberId(),
                pin.getMember().getNickname(),
                pin.getListenedDate(),
                pin.getMemo(),
                pin.getVisibility(),
                pin.getPlace().getPlaceName()
        );
    }
    public boolean isMine(Long currentUserId) {
        return this.creatorId.equals(currentUserId);
    }
}
