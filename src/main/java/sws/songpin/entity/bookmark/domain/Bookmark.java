package sws.songpin.entity.bookmark.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.playlist.domain.Playlist;
import sws.songpin.global.BaseTimeEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bookmark extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", updatable = false)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @Builder
    public Bookmark(Long bookmarkId, Member member, Playlist playlist) {
        this.bookmarkId = bookmarkId;
        this.member = member;
        this.playlist = playlist;
    }
}
