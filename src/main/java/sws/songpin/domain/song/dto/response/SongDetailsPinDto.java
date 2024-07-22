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
        double latitude,
        double longitude,
        Boolean isMine) {

    public static SongDetailsPinDto from(Pin pin, Boolean isMine) {
        return new SongDetailsPinDto(
                pin.getPinId(),
                pin.getMember().getMemberId(),
                pin.getMember().getNickname(),
                pin.getListenedDate(),
                pin.getMemo(),
                pin.getVisibility(),
                pin.getPlace().getPlaceName(),
                pin.getPlace().getLatitude( ),
                pin.getPlace().getLongitude(),
                isMine
        );
    }
}
