package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.playlist.entity.Playlist;

public record PlaylistCreateResponseDto(
        Long playlistId) {

    public static PlaylistCreateResponseDto from(Playlist playlist) {
        return new PlaylistCreateResponseDto(playlist.getPlaylistId());
    }
}
