package sws.songpin.domain.playlistPin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlistPin.entity.PlaylistPin;

import java.util.List;

public interface PlaylistPinRepository extends JpaRepository<PlaylistPin, Long> {
    List<PlaylistPin> findAllByPlaylist(Playlist playlist);
}
