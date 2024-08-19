package sws.songpin.domain.playlist.dto.response;

import java.util.List;

public record PlaylistMainResponseDto(
        int recentPlaylistsCount,
        List<PlaylistUnitDto> recentPlaylists,
        int followingPlaylistsCount,
        List<PlaylistUnitDto> followingPlaylists) {

    public static PlaylistMainResponseDto from(List<PlaylistUnitDto> recentPlaylists, List<PlaylistUnitDto> followingPlaylists) {
        return new PlaylistMainResponseDto(
                recentPlaylists.size(),
                recentPlaylists,
                followingPlaylists.size(),
                followingPlaylists
        );
    }
}
