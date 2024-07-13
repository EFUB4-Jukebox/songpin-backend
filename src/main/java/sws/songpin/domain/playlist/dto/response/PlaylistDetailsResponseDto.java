package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.song.dto.response.SongInfoDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PlaylistDetailsResponseDto(
        Boolean isMine,
        String playlistName,
        Long creatorId,
        String creatorNickname,
        LocalDateTime updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        Long bookmarkId,
        List<PlaylistPinListDto> pinList) {

    public static PlaylistDetailsResponseDto from(Playlist playlist, List<String> imgPathList, List<PlaylistPinListDto> pinList, Boolean isMine, Long bookmarkId) {
        return new PlaylistDetailsResponseDto(
                isMine,
                playlist.getPlaylistName(),
                playlist.getCreator().getMemberId(),
                playlist.getCreator().getNickname(),
                playlist.getModifiedTime(),
                playlist.getVisibility(),
                imgPathList,
                bookmarkId,
                pinList
        );
    }

    public record PlaylistPinListDto(
            Long playlistPinId,
            Long pinId,
            SongInfoDto songInfo,
            LocalDate listenedDate,
            String placeName,
            Long providerAddressId,
            GenreName genreName,
            int pinIndex) {
    }

}
