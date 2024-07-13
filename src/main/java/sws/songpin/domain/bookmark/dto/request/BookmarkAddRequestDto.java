package sws.songpin.domain.bookmark.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.playlist.entity.Playlist;

public record BookmarkAddRequestDto(
    @NotNull
    Long playlistId){

    public Bookmark toEntity(Member member, Playlist playlist){
        return Bookmark.builder()
                .bookmarkId(null)
                .member(member)
                .playlist(playlist)
                .build();

    }
}
