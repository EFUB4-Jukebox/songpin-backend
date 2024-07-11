package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Visibility;

import java.time.LocalDate;

public record PinResponseDto(
        Long pinId,
        LocalDate listenedDate,
        String memo,
        Visibility visibility,
        String placeName,
        GenreName genreName) {

    public static PinResponseDto from(Pin pin) {
        return new PinResponseDto(
                pin.getPinId(),
                pin.getListenedDate(),
                pin.getMemo(),
                pin.getVisibility(),
                pin.getPlace().getPlaceName(),
                pin.getGenre().getGenreName()
        );
    }
}
