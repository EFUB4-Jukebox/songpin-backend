package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.song.dto.response.SongInfoDto;
import java.time.LocalDate;

public record PlaylistPinUnitDto(
        Long playlistPinId,
        Long pinId,
        SongInfoDto songInfo,
        LocalDate listenedDate,
        String placeName,
        double placeLatitude,
        double placeLongitude,
        GenreName genreName,
        int pinIndex
) {
    public static PlaylistPinUnitDto from (PlaylistPin playlistPin) {
        SongInfoDto songInfo = SongInfoDto.from(playlistPin.getPin().getSong());
            return new PlaylistPinUnitDto(
                playlistPin.getPlaylistPinId(),
                playlistPin.getPin().getPinId(),
                songInfo,
                playlistPin.getPin().getListenedDate(),
                playlistPin.getPin().getPlace().getPlaceName(),
                playlistPin.getPin().getPlace().getLatitude(),
                playlistPin.getPin().getPlace().getLongitude(),
                playlistPin.getPin().getGenre().getGenreName(),
                playlistPin.getPinIndex()
        );
    }
}
