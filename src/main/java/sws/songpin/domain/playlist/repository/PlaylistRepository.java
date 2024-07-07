package sws.songpin.domain.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.playlist.entity.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
