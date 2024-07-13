package sws.songpin.domain.playlist.dto.response;

import java.util.List;

public record AllBookmarkResponseDto(
        int bookmarkCount,
        List<PlaylistListDto> bookmarkList){

    public static AllBookmarkResponseDto from(List<PlaylistListDto> bookmarkList) {
        return new AllBookmarkResponseDto(bookmarkList.size(), bookmarkList);
    }
}
