package sws.songpin.domain.playlistpin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;

import java.util.List;

public interface PlaylistPinRepository extends JpaRepository<PlaylistPin, Long> {
    List<PlaylistPin> findAllByPlaylist(Playlist playlist);
}
