package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.model.Visibility;

import java.time.LocalDate;

public record SongDetailsPinDto(
        Long pinId,
        Long creatorId,
        String creatorNickname,
        LocalDate listenedDate,
        String memo,
        Visibility visibility,
        String placeName,
        Boolean isMine) {

    public static SongDetailsPinDto from(Pin pin, Long currentMemberId) {
        // currentMemberId와 핀의 생성자가 일치하면 true
        Boolean isMine = currentMemberId != null && pin.getMember().getMemberId().equals(currentMemberId);
        return new SongDetailsPinDto(
                pin.getPinId(),
                pin.getMember().getMemberId(),
                pin.getMember().getNickname(),
                pin.getListenedDate(),
                pin.getMemo(),
                pin.getVisibility(),
                pin.getPlace().getPlaceName(),
                isMine
        );
    }
}
