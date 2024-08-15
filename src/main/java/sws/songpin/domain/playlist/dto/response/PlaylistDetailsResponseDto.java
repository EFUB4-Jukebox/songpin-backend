package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

import java.time.LocalDate;
import java.util.List;

public record PlaylistDetailsResponseDto(
        Boolean isMine,
        String playlistName,
        Long creatorId,
        String creatorNickname,
        int pinCount,
        LocalDate updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        boolean isBookmarked,
        List<PlaylistPinUnitDto> pinList) {

    public static PlaylistDetailsResponseDto from(Playlist playlist, List<String> imgPathList, List<PlaylistPinUnitDto> pinList, boolean isMine, boolean isBookmarked) {
        return new PlaylistDetailsResponseDto(
                isMine,
                playlist.getPlaylistName(),
                playlist.getCreator().getMemberId(),
                playlist.getCreator().getNickname(),
                pinList.size(),
                playlist.getModifiedTime().toLocalDate(),
                playlist.getVisibility(),
                imgPathList,
                isBookmarked,
                pinList
        );
    }

}
