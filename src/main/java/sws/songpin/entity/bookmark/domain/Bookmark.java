package sws.songpin.entity.bookmark.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.playlist.domain.Playlist;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", updatable = false)
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", updatable = false)
    @NotNull
    private Playlist playlist;

    @Builder
    public Bookmark(Long bookmarkId, Member member, Playlist playlist) {
        this.bookmarkId = bookmarkId;
        this.member = member;
        this.playlist = playlist;
    }
}
