package sws.songpin.entity.playlist.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.global.BaseTimeEntity;

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

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @Column(name = "playlist_name", nullable = false)
    private String playlistName;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Playlist(Long playlistId, Member creator, String playlistName) {
        this.playlistId = playlistId;
        this.creator = creator;
        this.playlistName = playlistName;
    }
}
