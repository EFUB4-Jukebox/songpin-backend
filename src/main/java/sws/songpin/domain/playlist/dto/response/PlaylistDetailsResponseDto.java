package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PlaylistDetailsResponseDto(
        Boolean isMine,
        String playlistName,
        Long creatorId,
        String creatorNickname,
        LocalDate updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        Long bookmarkId,
        List<PlaylistPinUnitDto> pinList) {

    public static PlaylistDetailsResponseDto from(Playlist playlist, List<String> imgPathList, List<PlaylistPinUnitDto> pinList, Boolean isMine, Long bookmarkId) {
        LocalDate updatedDate = (playlist.getModifiedTime() != null)
                ?playlist.getModifiedTime().toLocalDate()
                :playlist.getCreatedTime().toLocalDate();
        return new PlaylistDetailsResponseDto(
                isMine,
                playlist.getPlaylistName(),
                playlist.getCreator().getMemberId(),
                playlist.getCreator().getNickname(),
                updatedDate,
                playlist.getVisibility(),
                imgPathList,
                bookmarkId,
                pinList
        );
    }

}
