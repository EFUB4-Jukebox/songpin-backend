package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;

public record SongDetailsResponseDto(
        Long songId,
        String title,
        String artist,
        String imgPath,
        GenreName avgGenreName,
        int pinCount
) {
    public static SongDetailsResponseDto from(Song song, int pinCount) {
        return new SongDetailsResponseDto(
                song.getSongId(),
                song.getTitle(),
                song.getArtist(),
                song.getImgPath(),
                song.getAvgGenreName(),
                pinCount
        );
    }
}

