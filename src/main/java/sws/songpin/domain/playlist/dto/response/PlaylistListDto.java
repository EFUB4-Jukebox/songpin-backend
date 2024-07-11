package sws.songpin.domain.playlist.dto.response;
import sws.songpin.domain.pin.entity.Visibility;
import java.time.LocalDateTime;
import java.util.List;

public record PlaylistListDto(
        Long playlistId,
        String playlistName,
        String creatorNickname,
        int pinCount,
        LocalDateTime updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        boolean isBookmarked) {
}
