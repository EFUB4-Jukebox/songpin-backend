package sws.songpin.domain.playlist.dto.response;

public record PlaylistPinUpdateDto(
        Long playlistPinId,
        int pinIndex
) {
}
