package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

import java.time.LocalDateTime;
import java.util.List;

public record PlaylistUnitDto(
        // PlaylistListResponse, BookmarkListResponse에서 사용
        Long playlistId,
        String playlistName,
        String creatorNickname,
        int pinCount,
        LocalDateTime updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        Long bookmarkId
) {
    public static PlaylistUnitDto from (Playlist playlist, List<String> imgPathList, Long bookmarkId) {
        return new PlaylistUnitDto(
                playlist.getPlaylistId(),
                playlist.getPlaylistName(),
                playlist.getCreator().getNickname(),
                playlist.getPlaylistPins().size(),
                playlist.getModifiedTime(),
                playlist.getVisibility(),
                imgPathList,
                bookmarkId
        );
    }
}
