package sws.songpin.domain.song.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.song.entity.Song;

public record SongAddRequestDto(
        @NotNull String title,
        @NotNull String artist,
        @NotNull String imgPath,
        @NotNull String providerTrackCode
) {
    public Song toEntity() {
        return Song.builder()
                .title(this.title)
                .artist(this.artist)
                .imgPath(this.imgPath)
                .providerTrackCode(this.providerTrackCode)
                .build();
    }
}

