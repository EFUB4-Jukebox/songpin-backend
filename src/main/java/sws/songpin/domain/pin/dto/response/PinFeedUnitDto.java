package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.song.dto.response.SongInfoDto;
import java.time.LocalDate;

public record PinFeedUnitDto(
        Long pinId,
        SongInfoDto songInfo,
        LocalDate listenedDate,
        String placeName,
        GenreName genreName,
        String memo,
        Visibility visibility,
        Boolean isMine
) {
    public static PinFeedUnitDto from(Pin pin, Boolean isMine) {
        return new PinFeedUnitDto(
                pin.getPinId(),
                SongInfoDto.from(pin.getSong()),
                pin.getListenedDate(),
                pin.getPlace().getPlaceName(),
                pin.getGenre().getGenreName(),
                pin.getMemo(),
                pin.getVisibility(),
                isMine
        );
    }
}
