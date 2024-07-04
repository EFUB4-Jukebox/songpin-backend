package sws.songpin.entity.playlistPin.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.entity.playlist.domain.Playlist;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlaylistPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_pin_id", updatable = false)
    private Long playlistPinId;

    @Column(name = "pin_index", nullable = false)
    private int pinIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id", nullable = false)
    private Pin pin;

    @Builder
    public PlaylistPin(Long playlistPinId, int pinIndex, Playlist playlist, Pin pin) {
        this.playlistPinId = playlistPinId;
        this.pinIndex = pinIndex;
        this.playlist = playlist;
        this.pin = pin;
    }
}
