package sws.songpin.domain.song.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sws.songpin.domain.song.entity.Song;

//public record SongRequestDto(
//        Long songId,
//        @NotNull String title,
//        @NotNull String artist,
//        @NotNull String imgPath,
//        @NotNull String providerTrackCode
//) {
//    public SongRequestDto(@NotNull String title, @NotNull String artist, @NotNull String imgPath, @NotNull String providerTrackCode) {
//        this(null, title, artist, imgPath, providerTrackCode);
//    }
//
//    public Song toEntity() {
//        return Song.builder()
//                .songId(this.songId())
//                .title(this.title())
//                .artist(this.artist())
//                .imgPath(this.imgPath())
//                .providerTrackCode(this.providerTrackCode())
//                .build();
//    }
//}

@Getter
@NoArgsConstructor
public class SongRequestDto {
    private Long songId;
    @NotNull
    private String title;
    @NotNull
    private String artist;
    @NotNull
    private String imgPath;
    @NotNull
    private String providerTrackCode;

    // 모든 필드 포함
    public SongRequestDto(Long songId, @NotNull String title, @NotNull String artist, @NotNull String imgPath, @NotNull String providerTrackCode) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.imgPath = imgPath;
        this.providerTrackCode = providerTrackCode;
    }

    // songId 제외
    public SongRequestDto(@NotNull String title, @NotNull String artist, @NotNull String imgPath, @NotNull String providerTrackCode) {
        this(null, title, artist, imgPath, providerTrackCode);
    }

    public Song toEntity() {
        return Song.builder()
                .songId(this.songId)
                .title(this.title)
                .artist(this.artist)
                .imgPath(this.imgPath)
                .providerTrackCode(this.providerTrackCode)
                .build();
    }
}

