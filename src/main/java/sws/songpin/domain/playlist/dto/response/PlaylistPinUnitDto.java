package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.song.dto.response.SongInfoDto;
import sws.songpin.domain.song.entity.Song;

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
    public static PlaylistPinUnitDto from (PlaylistPin playlistPin, Pin pin, Song song, Place place, GenreName genreName) {
        SongInfoDto songInfo = SongInfoDto.from(song);
            return new PlaylistPinUnitDto(
                    playlistPin.getPlaylistPinId(),
                    pin.getPinId(),
                    songInfo,
                    pin.getListenedDate(),
                    place.getPlaceName(),
                    place.getLatitude(),
                    place.getLongitude(),
                    genreName,
                    playlistPin.getPinIndex()
        );
    }
}
