package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;

import java.time.LocalDate;

public record PinExistingInfoResponseDto(
        String songImgPath,
        String songTitle,
        String songArtist,
        LocalDate listenedDate,
        String placeName,
        GenreName genreName,
        String memo,
        Visibility visibility
) {
    public static PinExistingInfoResponseDto from(Pin pin, GenreName genreName) {
        return new PinExistingInfoResponseDto(
                pin.getSong().getImgPath(),
                pin.getSong().getTitle(),
                pin.getSong().getArtist(),
                pin.getListenedDate(),
                pin.getPlace().getPlaceName(),
                genreName,
                pin.getMemo(),
                pin.getVisibility()
        );
    }
}