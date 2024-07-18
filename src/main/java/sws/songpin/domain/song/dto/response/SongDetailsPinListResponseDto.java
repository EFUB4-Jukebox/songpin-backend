package sws.songpin.domain.song.dto.response;

import java.util.List;

public record SongDetailsPinListResponseDto(
    List<SongDetailsPinDto> songDetailsPinList){

    public static SongDetailsPinListResponseDto from(List<SongDetailsPinDto> songDetailsPinList) {
        return new SongDetailsPinListResponseDto(
                songDetailsPinList
        );
    }
}
