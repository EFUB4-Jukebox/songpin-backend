package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.song.dto.response.SongInfoDto;

import java.time.LocalDate;

// 홈, 마이페이지 캘린더, 마이페이지 검색에서 사용
public record PinBasicUnitDto(
        Long pinId,
        SongInfoDto songInfo,
        LocalDate listenedDate,
        String placeName,
        double latitude,
        double longitude,
        GenreName genreName,
        Boolean isMine

) {
    public static PinBasicUnitDto from(Pin pin, Boolean isMine) {
        return new PinBasicUnitDto(
                pin.getPinId(),
                SongInfoDto.from(pin.getSong()),
                pin.getListenedDate(),
                pin.getPlace().getPlaceName(),
                pin.getPlace().getLatitude( ),
                pin.getPlace().getLongitude(),
                pin.getGenre().getGenreName(),
                isMine
        );
    }
}