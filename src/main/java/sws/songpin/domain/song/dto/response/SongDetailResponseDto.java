package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.dto.response.PinResponseDto;
import sws.songpin.domain.song.entity.Song;

import java.util.List;

public record SongDetailResponseDto(
        Long songId,
        String title,
        String artist,
        String imgPath,
        GenreName avgGenre,
        int pinCount,
        List<PinResponseDto> pins
) {
    public static SongDetailResponseDto from(Song song, List<PinResponseDto> pins) {
        return new SongDetailResponseDto(
                song.getSongId(),
                song.getTitle(),
                song.getArtist(),
                song.getImgPath(),
                song.getAvgGenreName(),
                pins.size(),
                pins
        );
    }
}

