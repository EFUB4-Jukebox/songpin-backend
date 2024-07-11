package sws.songpin.domain.song.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.song.entity.Song;

public record SongRequestDto(
        Long songId,
        @NotNull String title,
        @NotNull String artist,
        @NotNull String imgPath,
        @NotNull String providerTrackCode
) {
    public SongRequestDto(@NotNull String title, @NotNull String artist, @NotNull String imgPath, @NotNull String providerTrackCode) {
        this(null, title, artist, imgPath, providerTrackCode);
    }

    public Song toEntity() {
        return Song.builder()
                .songId(this.songId())
                .title(this.title())
                .artist(this.artist())
                .imgPath(this.imgPath())
                .providerTrackCode(this.providerTrackCode())
                .build();
    }
}

