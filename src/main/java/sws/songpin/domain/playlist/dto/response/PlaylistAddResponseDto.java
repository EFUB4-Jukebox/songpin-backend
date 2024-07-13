package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.playlist.entity.Playlist;

public record PlaylistAddResponseDto(
        Long playlistId) {

    public static PlaylistAddResponseDto from(Playlist playlist) {
        return new PlaylistAddResponseDto(playlist.getPlaylistId());
    }
}
