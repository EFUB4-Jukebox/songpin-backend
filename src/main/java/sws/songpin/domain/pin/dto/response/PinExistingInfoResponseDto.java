package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;

import java.time.LocalDate;

public record PinExistingInfoResponseDto(
        String songImgPath,
        String title,
        String artist,
        LocalDate listenedDate,
        String placeName,
        GenreName genreName,
        String memo,
        Visibility visibility
) {
    public static PinExistingInfoResponseDto from(Pin pin) {
        return new PinExistingInfoResponseDto(
                pin.getSong().getImgPath(),
                pin.getSong().getTitle(),
                pin.getSong().getArtist(),
                pin.getListenedDate(),
                pin.getPlace().getPlaceName(),
                pin.getGenre().getGenreName(),
                pin.getMemo(),
                pin.getVisibility()
        );
    }
}