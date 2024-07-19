package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.model.Visibility;

import java.time.LocalDate;

public record SongDetailsPinDto(
        Long pinId,
        String creatorNickname,
        LocalDate listenedDate,
        String memo,
        Visibility visibility,
        String placeName,
        Boolean isMine) {

    public static SongDetailsPinDto from(Pin pin, Boolean isMine) {
        return new SongDetailsPinDto(
                pin.getPinId(),
                pin.getMember().getNickname(),
                pin.getListenedDate(),
                pin.getMemo(),
                pin.getVisibility(),
                pin.getPlace().getPlaceName(),
                isMine
        );
    }
}
