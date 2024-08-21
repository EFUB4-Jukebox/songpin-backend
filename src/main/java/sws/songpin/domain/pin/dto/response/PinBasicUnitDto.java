package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.dto.response.SongInfoDto;
import sws.songpin.domain.song.entity.Song;

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
    public static PinBasicUnitDto from(Pin pin, Song song, Place place, GenreName genreName, boolean isMine) {
        return new PinBasicUnitDto(
                pin.getPinId(),
                SongInfoDto.from(song),
                pin.getListenedDate(),
                place.getPlaceName(),
                place.getLatitude(),
                place.getLongitude(),
                genreName,
                isMine
        );
    }
}