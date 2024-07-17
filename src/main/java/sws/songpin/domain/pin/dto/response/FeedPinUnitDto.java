package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.song.dto.response.SongInfoDto;
import java.time.LocalDate;

public record FeedPinUnitDto(
        Long PinId,
        Long CreatorId,
        SongInfoDto songInfo,
        LocalDate listenedDate,
        String placeName,
        GenreName genreName,
        String memo,
        Visibility visibility,
        Boolean isMine,
        LocalDate updatedDate
) {
    public static FeedPinUnitDto from(Pin pin, Boolean isMine) {
        return new FeedPinUnitDto(
                pin.getPinId(),
                pin.getMember().getMemberId(),
                SongInfoDto.from(pin.getSong()),
                pin.getListenedDate(),
                pin.getPlace().getPlaceName(),
                pin.getGenre().getGenreName(),
                pin.getMemo(),
                pin.getVisibility(),
                isMine,
                pin.getModifiedTime().toLocalDate()
        );
    }
}
