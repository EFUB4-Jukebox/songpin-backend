package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;

import java.time.LocalDate;

public record SongDetailsResponseDto(
        Long songId,
        String title,
        String artist,
        String imgPath,
        GenreName avgGenreName,
        LocalDate lastListenedDate
) {
    public static SongDetailsResponseDto from(Song song, LocalDate lastListenedDate) {
        return new SongDetailsResponseDto(
                song.getSongId(),
                song.getTitle(),
                song.getArtist(),
                song.getImgPath(),
                song.getAvgGenreName(),
                lastListenedDate
        );
    }
}

