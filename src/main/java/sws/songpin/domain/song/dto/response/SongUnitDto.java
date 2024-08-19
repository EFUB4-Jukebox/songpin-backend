package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;

public record SongUnitDto(
        SongInfoDto songInfo,
        GenreName avgGenreName,
        int pinCount
) {
    public static SongUnitDto from(Song song, int pinCount) {
        return new SongUnitDto(
                SongInfoDto.from(song),
                song.getAvgGenreName(),
                pinCount
        );
    }
}
