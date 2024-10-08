package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.member.entity.Member;
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
        Boolean isBookmarked
) {
    public static PlaylistUnitDto from (Playlist playlist, Member creator, int pinCount, List<String> imgPathList, boolean isBookmarked) {
        return new PlaylistUnitDto(
                playlist.getPlaylistId(),
                playlist.getPlaylistName(),
                creator.getNickname(),
                pinCount,
                playlist.getModifiedTime(),
                playlist.getVisibility(),
                imgPathList,
                isBookmarked
        );
    }
}
