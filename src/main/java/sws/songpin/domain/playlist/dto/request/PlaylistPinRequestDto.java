package sws.songpin.domain.playlist.dto.request;

public record PlaylistPinRequestDto(
        Long playlistId,
        Long pinId
) {
}
