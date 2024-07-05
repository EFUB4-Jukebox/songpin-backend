package sws.songpin.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.song.entity.Song;

import java.util.List;

public interface PinRepository extends JpaRepository <Pin, Long> {
    List<Pin> findAllBySong(Song song);
}
