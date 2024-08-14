package sws.songpin.domain.bookmark.dto.response;

import sws.songpin.domain.bookmark.entity.Bookmark;

public record BookmarkChangeResponseDto(
    boolean isBookmarked,
    Long bookmarkId){

    public static BookmarkChangeResponseDto from(boolean isBookmarked, Bookmark bookmark) {
        if (isBookmarked) {
            return new BookmarkChangeResponseDto(isBookmarked, null);
        }
        return new BookmarkChangeResponseDto(isBookmarked, bookmark.getBookmarkId());
    }
}
