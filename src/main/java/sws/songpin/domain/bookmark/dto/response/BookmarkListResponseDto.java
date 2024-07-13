package sws.songpin.domain.bookmark.dto.response;

import sws.songpin.domain.playlist.dto.response.PlaylistUnitDto;

import java.util.List;

public record BookmarkListResponseDto(
        int bookmarkCount,
        List<PlaylistUnitDto> bookmarkList){

    public static BookmarkListResponseDto from(List<PlaylistUnitDto> bookmarkList) {
        return new BookmarkListResponseDto(bookmarkList.size(), bookmarkList);
    }
}
