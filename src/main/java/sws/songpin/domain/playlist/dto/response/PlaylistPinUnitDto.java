package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.dto.response.SongInfoDto;
import java.time.LocalDate;

public record PlaylistPinUnitDto(
        Long playlistPinId,
        Long pinId,
        SongInfoDto songInfo,
        LocalDate listenedDate,
        String placeName,
        Long providerAddressId,
        GenreName genreName,
        int pinIndex) {
}
