package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;

public record SongDetailResponseDto(
        Long songId,
        String title,
        String artist,
        String imgPath,
        GenreName avgGenre,
        int pinCount
) {
    public static SongDetailResponseDto from(Song song, int pinCount) {
        return new SongDetailResponseDto(
                song.getSongId(),
                song.getTitle(),
                song.getArtist(),
                song.getImgPath(),
                song.getAvgGenreName(),
                pinCount
        );
    }
}

