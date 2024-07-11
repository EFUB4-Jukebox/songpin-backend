package sws.songpin.domain.bookmark.dto.response;

import sws.songpin.domain.bookmark.entity.Bookmark;

public record BookmarkResponseDto(
        Long bookmarkId) {

    public static BookmarkResponseDto from(Bookmark bookmark) {
        return new BookmarkResponseDto(bookmark.getBookmarkId());
    }

}
