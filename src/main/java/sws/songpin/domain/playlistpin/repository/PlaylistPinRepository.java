package sws.songpin.domain.playlistpin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;

import java.util.List;
import java.util.Optional;

public interface PlaylistPinRepository extends JpaRepository<PlaylistPin, Long> {
    List<PlaylistPin> findAllByPlaylist(Playlist playlist);
    Optional<Boolean> existsByPlaylistAndPin(Playlist playlist, Pin pin);
}
