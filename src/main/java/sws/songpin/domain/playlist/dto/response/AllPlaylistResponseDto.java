package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.pin.entity.Visibility;
import java.time.LocalDateTime;
import java.util.List;

public record AllPlaylistResponseDto(
        int playlistCount,
        List<UserPlaylistDto> playlistList){

    public static AllPlaylistResponseDto from(List<UserPlaylistDto> playlistList) {
        return new AllPlaylistResponseDto(
                playlistList.size(),
                playlistList
        );
    }

    public record UserPlaylistDto(
            String playlistName,
            Long creatorId,
            String creatorNickname,
            int pinCount,
            LocalDateTime updatedDate,
            Visibility visibility,
            List<String> imgPathList,
            boolean isBookmarked) {
    }
}
