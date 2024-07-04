package sws.songpin.domain.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.song.entity.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
}
