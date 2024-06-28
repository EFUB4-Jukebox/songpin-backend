package sws.songpin.entity.playlistPin.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.entity.playlist.domain.Playlist;
import sws.songpin.global.BaseTimeEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlaylistPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_pin_id", updatable = false)
    private Long playlistPinId;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "pin_id", nullable = false)
    private Pin pin;

    @Column(name = "pin_index", nullable = false)
    private int pinIndex;

    @Builder
    public PlaylistPin(Long playlistPinId, Playlist playlist, Pin pin, int pinIndex) {
        this.playlistPinId = playlistPinId;
        this.playlist = playlist;
        this.pin = pin;
        this.pinIndex = pinIndex;
    }
}
