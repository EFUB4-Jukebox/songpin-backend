package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;

public record SongInfoResponseDto(
        Long songId,
        String title,
        String artist,
        String imgPath
) {
    public static SongInfoResponseDto from(Song song) {
        return new SongInfoResponseDto(
                song.getSongId(),
                song.getTitle(),
                song.getArtist(),
                song.getImgPath()
        );
    }
}
