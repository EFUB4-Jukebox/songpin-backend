package sws.songpin.domain.pin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.entity.Song;

import java.util.List;
import java.util.Optional;

public interface PinRepository extends JpaRepository <Pin, Long> {
    List<Pin> findBySong(Song song);
    Page<Pin> findAllBySong(Song song, Pageable pageable);

    int countBySong(Song song);
    // 노래 상세보기 페이지에서 "들은날짜" = 등록한 핀 중 가장 최신의 listenedDate
    Optional<Pin> findTopBySongAndCreatorOrderByListenedDateDesc(Song song, Member member);
    Page<Pin> findAllBySongAndCreator(Song song, Member creator, Pageable pageable);
    @Query("SELECT p.pinId, p.song.songId, p.song.title, p.song.artist, p.song.imgPath, p.listenedDate, p.place.placeName, p.place.latitude, p.place.longitude, p.genre.genreName, p.creator.memberId, p.memo, p.visibility " +
            "FROM Pin p WHERE p.creator = :creator ORDER BY p.listenedDate DESC")
    Page<Pin> findPinFeed(@Param("creator") Member creator, Pageable pageable);
    List<Pin> findAllByPlace(Place place);
    @Query("SELECT p FROM Pin p WHERE p.creator = :creator AND YEAR(p.listenedDate) = :year AND MONTH(p.listenedDate) = :month")
    List<Pin> findAllByCreatorAndDate(@Param("creator") Member creator, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(p) FROM Pin p WHERE YEAR(p.listenedDate) = :currentYear")
    long countByListenedDateYear(@Param("currentYear") int currentYear);
    @Query("SELECT p.genre.genreName, COUNT(p) FROM Pin p GROUP BY p.genre ORDER BY COUNT(p) DESC")
    List<Object[]> findMostPopularGenreName();

    @Query(value = "SELECT * FROM pin ORDER BY pin_id DESC LIMIT 3", nativeQuery = true)
    List<Pin> findTop3ByOrderByPinIdDesc();

    @Query(value = "SELECT p.pin_id, s.song_id, s.title, s.artist, s.img_path, p.listened_date, pl.place_name, pl.latitude, pl.longitude, g.genre_name, p.creator_id " +
            "FROM pin p " +
            "LEFT JOIN song s ON p.song_id = s.song_id " +
            "LEFT JOIN place pl ON p.place_id = pl.place_id " +
            "LEFT JOIN genre g ON s.avg_genre_name = g.genre_name " +
            "WHERE (REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces%) " +
            "AND p.creator_id = :currentMemberId " +
            "ORDER BY s.title ASC", // 정확도 순 정렬
            countQuery = "SELECT COUNT(p.pin_id) " +
                    "FROM pin p " +
                    "LEFT JOIN song s ON p.song_id = s.song_id " +
                    "WHERE (REPLACE(s.title, ' ', '') LIKE %:keywordNoSpaces% OR REPLACE(s.artist, ' ', '') LIKE %:keywordNoSpaces%) " +
                    "AND p.creator_id = :currentMemberId",
            nativeQuery = true)
    Page<Object[]> findAllBySongNameOrArtistContainingIgnoreSpaces(@Param("currentMemberId") Long currentMemberId, @Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);
}
