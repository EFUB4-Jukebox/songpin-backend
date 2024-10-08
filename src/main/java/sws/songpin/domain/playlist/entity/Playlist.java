package sws.songpin.domain.playlist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.global.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @Column(name = "playlist_name", length = 40)
    @NotNull
    private String playlistName;

    @Column(name = "visibility", length = 10)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", updatable = false)
    @NotNull
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

    public void updatePlaylistName(String playlistName){
        this.playlistName = playlistName;
    }

    public void updateVisibility(Visibility visibility){
        this.visibility = visibility;
    }

    public void removePlaylistPin(PlaylistPin currentPin) {
        playlistPins.remove(currentPin);
        currentPin.setPlaylist(null);
    }

    public void addPlaylistPin(PlaylistPin playlistPin) {
        this.playlistPins.add(playlistPin);
    }
}
