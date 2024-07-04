package sws.songpin.entity.playlist.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.bookmark.domain.Bookmark;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.pin.domain.Visibility;
import sws.songpin.entity.playlistPin.domain.PlaylistPin;
import sws.songpin.global.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Playlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id", updatable = false)
    private Long playlistId;

    @Column(name = "playlist_name", nullable = false)
    private String playlistName;

    @Column(name = "visibility", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false, updatable = false)
    private Member creator;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistPin> playlistPins;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks;

    @Builder
    public Playlist(Long playlistId, String playlistName, Visibility visibility, Member creator) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.visibility = visibility;
        this.creator = creator;
        this.playlistPins = new ArrayList<>();
        this.bookmarks = new ArrayList<>();
    }
}
