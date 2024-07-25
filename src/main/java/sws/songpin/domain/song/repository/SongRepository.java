package sws.songpin.domain.song.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.statistics.dto.projection.StatsSongProjectionDto;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByProviderTrackCode(String providerTrackCode);

    @Query("""
        SELECT new sws.songpin.domain.statistics.dto.projection.StatsSongProjectionDto(
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
        SELECT new sws.songpin.domain.statistics.dto.projection.StatsSongProjectionDto(
            s.songId, s.title, s.artist, s.imgPath, s.avgGenreName
        )
        FROM Song s
        LEFT JOIN s.pins p
        GROUP BY s.songId
        ORDER BY COUNT(p) DESC, s.songId DESC
    """)
    Slice<StatsSongProjectionDto> findTopSongs(Pageable pageable);

    @Query(value = "SELECT s.song_id, s.title, s.artist, s.img_path, s.avg_genre_name, COUNT(p.pin_id) AS pin_count " +
            "FROM song s " +
            "LEFT JOIN pin p ON s.song_id = p.song_id " +
            "WHERE REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% " +
            "OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY s.song_id" +
            "ORDER BY pin_count DESC, s.title ASC",
            countQuery = "SELECT COUNT(DISTINCT s.song_id) " +
                    "FROM song s " +
                    "LEFT JOIN pin p ON s.song_id = p.song_id " +
                    "WHERE REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% " +
                    "OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces%",
            nativeQuery = true)
    Page<Object[]> findAllBySongNameOrArtistContainingIgnoreSpacesOrderByCount(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT s.song_id, s.title, s.artist, s.img_path, s.avg_genre_name, COUNT(p.pin_id) AS pin_count " +
            "FROM song s " +
            "LEFT JOIN pin p ON s.song_id = p.song_id " +
            "WHERE REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% " +
            "OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY s.song_id " +
            "ORDER BY s.title ASC",
            countQuery = "SELECT COUNT(DISTINCT s.song_id) " +
                    "FROM song s " +
                    "LEFT JOIN pin p ON s.song_id = p.song_id " +
                    "WHERE REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% " +
                    "OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces%",
            nativeQuery = true)
    Page<Object[]> findAllBySongNameOrArtistContainingIgnoreSpacesOrderByAccuracy(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT s.song_id, s.title, s.artist, s.img_path, s.avg_genre_name, COUNT(p.pin_id) AS pin_count, MAX(p.created_time) as newest_time " +
            "FROM song s " +
            "LEFT JOIN pin p ON s.song_id = p.song_id " +
            "WHERE REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% " +
            "OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY s.song_id " +
            "ORDER BY MAX(pin.created_time) DESC",
            countQuery = "SELECT COUNT(DISTINCT s.song_id) " +
                    "FROM song s " +
                    "LEFT JOIN pin p ON s.song_id = p.song_id " +
                    "WHERE REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% " +
                    "OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces%",
            nativeQuery = true)
    Page<Object[]> findAllBySongNameOrArtistContainingIgnoreSpacesOrderByNewest(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

}
