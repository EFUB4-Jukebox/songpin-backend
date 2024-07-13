package sws.songpin.domain.bookmark.dto.response;

import sws.songpin.domain.bookmark.entity.Bookmark;

public record BookmarkAddResponseDto(
        Long bookmarkId) {

    public static BookmarkAddResponseDto from(Bookmark bookmark) {
        return new BookmarkAddResponseDto(bookmark.getBookmarkId());
    }

}
