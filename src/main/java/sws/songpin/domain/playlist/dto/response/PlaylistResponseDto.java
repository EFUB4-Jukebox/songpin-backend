package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PlaylistResponseDto(
        Boolean isMine,
        String playlistName,
        Long creatorId,
        String creatorNickname,
        LocalDateTime updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        Long bookmarkId,
        List<PlaylistPinListDto> pinList) {

    public static PlaylistResponseDto from(Playlist playlist, List<String> imgPathList, List<PlaylistPinListDto> pinList) {
        return new PlaylistResponseDto(
                false, // isMine (추후 추가)
                playlist.getPlaylistName(),
                playlist.getCreator().getMemberId(),
                playlist.getCreator().getNickname(),
                playlist.getModifiedTime(),
                playlist.getVisibility(),
                imgPathList,
                null, // bookmarkId (추후 추가)
                pinList
        );
    }

    public record PlaylistPinListDto(
            Long playlistPinId,
            Long pinId,
            SongInfo songInfo,
            LocalDate listenedDate,
            String placeName,
            Long providerAddressId,
            GenreName genreName,
            int pinIndex) {
    }

    public record SongInfo(
            Long songId,
            String title,
            String artist,
            String imgPath) {
    }
}
