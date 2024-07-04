package sws.songpin.entity.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.entity.song.domain.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
}
