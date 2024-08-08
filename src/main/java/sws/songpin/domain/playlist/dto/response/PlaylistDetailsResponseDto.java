package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.member.entity.Status;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

import java.time.LocalDate;
import java.util.List;

public record PlaylistDetailsResponseDto(
        Boolean isMine,
        String playlistName,
        Long creatorId,
        String creatorNickname,
        Status creatorStatus,
        int pinCount,
        LocalDate updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        Long bookmarkId,
        List<PlaylistPinUnitDto> pinList) {

    public static PlaylistDetailsResponseDto from(Playlist playlist, List<String> imgPathList, List<PlaylistPinUnitDto> pinList, Boolean isMine, Long bookmarkId) {
        return new PlaylistDetailsResponseDto(
                isMine,
                playlist.getPlaylistName(),
                playlist.getCreator().getMemberId(),
                playlist.getCreator().getNickname(),
                playlist.getCreator().getStatus(),
                pinList.size(),
                playlist.getModifiedTime().toLocalDate(),
                playlist.getVisibility(),
                imgPathList,
                bookmarkId,
                pinList
        );
    }

}
