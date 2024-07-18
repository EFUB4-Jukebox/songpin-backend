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

    public static SongDetailsPinDto from(Pin pin, Long currentMemberId) {
        // currentMemberId와 핀의 생성자가 일치하면 true
        // SongDetailsPinDto로 전달해야해서 서비스로직으로 분리 불가
        Boolean isMine = currentMemberId != null && pin.getMember().getMemberId().equals(currentMemberId);
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
