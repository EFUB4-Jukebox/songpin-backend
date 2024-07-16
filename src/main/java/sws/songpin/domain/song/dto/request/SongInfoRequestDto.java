package sws.songpin.domain.song.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.genre.entity.GenreName;

public record SongInfoRequestDto(
        Long songId,
        @NotNull String title,
        @NotNull String artist,
        @NotNull String imgPath
) {
}

