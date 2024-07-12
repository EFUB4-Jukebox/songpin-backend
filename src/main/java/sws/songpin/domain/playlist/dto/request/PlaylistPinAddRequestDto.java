package sws.songpin.domain.playlist.dto.request;

import jakarta.validation.constraints.NotNull;

public record PlaylistPinAddRequestDto(
        @NotNull Long playlistId,
        @NotNull Long pinId
) {
}
