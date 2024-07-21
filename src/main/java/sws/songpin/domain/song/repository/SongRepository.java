package sws.songpin.domain.song.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.statistics.dto.response.StatsSongProjectionDto;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByProviderTrackCode(String providerTrackCode);

    @Query("""
        SELECT new sws.songpin.domain.statistics.dto.response.StatsSongProjectionDto(
            s.songId, s.title, s.artist, s.imgPath, s.avgGenreName
        )
        FROM Song s
        LEFT JOIN s.pins p
        WHERE s.avgGenreName = :genreName
        GROUP BY s.songId
        ORDER BY COUNT(p) DESC, s.songId DESC
    """)
    Slice<StatsSongProjectionDto> findTopSongsByGenreName(GenreName genreName, Pageable pageable);

    @Query("""
        SELECT new sws.songpin.domain.statistics.dto.response.StatsSongProjectionDto(
            s.songId, s.title, s.artist, s.imgPath, s.avgGenreName
        )
        FROM Song s
        LEFT JOIN s.pins p
        GROUP BY s.songId
        ORDER BY COUNT(p) DESC, s.songId DESC
    """)
    Slice<StatsSongProjectionDto> findTopSongs(Pageable pageable);
}
