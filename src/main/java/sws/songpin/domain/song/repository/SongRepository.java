package sws.songpin.domain.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.song.entity.Song;

import java.util.BitSet;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByTitleAndArtist(String title, String artist);
}
