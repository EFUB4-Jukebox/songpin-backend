package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.member.entity.Status;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.model.Visibility;

import java.time.LocalDate;

public record SongDetailsPinDto(
        Long pinId,
        String creatorHandle,
        String creatorNickname,
        Status creatorStatus,
        LocalDate listenedDate,
        String memo,
        Visibility visibility,
        String placeName,
        double latitude,
        double longitude,
        Boolean isMine) {

    public static SongDetailsPinDto from(Pin pin, String memo, Boolean isMine) {
        return new SongDetailsPinDto(
                pin.getPinId(),
                pin.getCreator().getHandle(),
                pin.getCreator().getNickname(),
                pin.getCreator().getStatus(),
                pin.getListenedDate(),
                memo,
                pin.getVisibility(),
                pin.getPlace().getPlaceName(),
                pin.getPlace().getLatitude( ),
                pin.getPlace().getLongitude(),
                isMine
        );
    }
}
