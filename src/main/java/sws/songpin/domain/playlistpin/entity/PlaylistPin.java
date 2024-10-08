package sws.songpin.domain.playlistpin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.playlist.entity.Playlist;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PlaylistPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_pin_id", updatable = false)
    private Long playlistPinId;

    @Column(name = "pin_index")
    @NotNull
    private int pinIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    @NotNull
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    @NotNull
    private Pin pin;

    @Builder
    public PlaylistPin(Long playlistPinId, int pinIndex, Playlist playlist, Pin pin) {
        this.playlistPinId = playlistPinId;
        this.pinIndex = pinIndex;
        this.playlist = playlist;
        this.pin = pin;
    }

    public void updatePinIndex(int pinIndex) {
        this.pinIndex = pinIndex;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
