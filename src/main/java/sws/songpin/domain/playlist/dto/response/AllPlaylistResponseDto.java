package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.pin.entity.Visibility;
import java.time.LocalDateTime;
import java.util.List;

public record AllPlaylistResponseDto(
        int playlistCount,
        List<PlaylistListDto> playlistList){

    public static AllPlaylistResponseDto from(List<PlaylistListDto> playlistList) {
        return new AllPlaylistResponseDto(
                playlistList.size(),
                playlistList
        );
    }
}
