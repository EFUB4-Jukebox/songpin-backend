package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.song.entity.Song;

public record SongInfoDto(
        Long songId,
        String title,
        String artist,
        String imgPath
) {
    public static SongInfoDto from(Song song) {
        return new SongInfoDto(
                song.getSongId(),
                song.getTitle(),
                song.getArtist(),
                song.getImgPath()
        );
    }
}
