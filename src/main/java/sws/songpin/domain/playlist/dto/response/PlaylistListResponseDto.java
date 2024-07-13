package sws.songpin.domain.playlist.dto.response;

import java.util.List;

public record PlaylistListResponseDto(
        int playlistCount,
        List<PlaylistUnitDto> playlistList){

    public static PlaylistListResponseDto from(List<PlaylistUnitDto> playlistList) {
        return new PlaylistListResponseDto(
                playlistList.size(),
                playlistList
        );
    }
}
