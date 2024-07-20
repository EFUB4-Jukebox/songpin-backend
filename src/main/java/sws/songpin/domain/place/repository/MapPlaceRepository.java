package sws.songpin.domain.place.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.place.dto.response.MapPlaceProjectionDto;
import sws.songpin.domain.place.entity.Place;

import java.time.LocalDate;
import java.util.Set;

public interface MapPlaceRepository extends JpaRepository<Place, Long> {

    // 좌표 범위에 포함되는 장소들 불러오기
    @Query("""
        SELECT new sws.songpin.domain.place.dto.response.MapPlaceProjectionDto(
            p.placeId,
            p.latitude,
            p.longitude,
            SUM(CASE WHEN pin.genre.genreName IN :genreNameSet THEN 1 ELSE 0 END),
            latestPin.listenedDate,
            latestPin.song.songId,
            latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p
        )
        WHERE p.latitude BETWEEN :swLat AND :neLat
        AND p.longitude BETWEEN :swLng AND :neLng
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.song.songId, latestPin.genre.genreName, latestPin.listenedDate
        HAVING SUM(CASE WHEN pin.genre.genreName IN :genreNameSet THEN 1 ELSE 0 END) > 0
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<MapPlaceProjectionDto> findPlacesWithLatestPinsByGenre(@Param("swLat") double swLat,
                                                                 @Param("neLat") double neLat,
                                                                 @Param("swLng") double swLng,
                                                                 @Param("neLng") double neLng,
                                                                 @Param("genreNameSet") Set<GenreName> genreNameSet,
                                                                 Pageable pageable);

    // 좌표 범위 & 기간 범위에 모두 포함되는 장소들 불러오기
    @Query("""
        SELECT new sws.songpin.domain.place.dto.response.MapPlaceProjectionDto(
            p.placeId,
            p.latitude,
            p.longitude,
            SUM(CASE WHEN pin.genre.genreName IN :genreNameSet THEN 1 ELSE 0 END),
            latestPin.listenedDate,
            latestPin.song.songId,
            latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p
        )
        WHERE p.latitude BETWEEN :swLat AND :neLat
        AND p.longitude BETWEEN :swLng AND :neLng
        AND pin.listenedDate BETWEEN :startDate AND :endDate
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.song.songId, latestPin.genre.genreName, latestPin.listenedDate
        HAVING SUM(CASE WHEN pin.genre.genreName IN :genreNameSet THEN 1 ELSE 0 END) > 0
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<MapPlaceProjectionDto> findPlacesWithLatestPinsByGenreAndDateRange(@Param("swLat") double swLat,
                                                                             @Param("neLat") double neLat,
                                                                             @Param("swLng") double swLng,
                                                                             @Param("neLng") double neLng,
                                                                             @Param("genreNameSet") Set<GenreName> genreNameSet,
                                                                             @Param("startDate") LocalDate startDate,
                                                                             @Param("endDate") LocalDate endDate,
                                                                             Pageable pageable);

    // 유저가 핀을 등록한 지도 장소들 불러오기
    @Query("""
        SELECT new sws.songpin.domain.place.dto.response.MapPlaceProjectionDto(
            p.placeId,
            p.latitude,
            p.longitude,
            SUM(CASE WHEN pin.member.memberId = :memberId THEN 1 ELSE 0 END),
            latestPin.listenedDate,
            latestPin.song.songId,
            latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p
        )
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.song.songId, latestPin.genre.genreName, latestPin.listenedDate
        HAVING SUM(CASE WHEN pin.member.memberId = :memberId THEN 1 ELSE 0 END) > 0
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<MapPlaceProjectionDto> findPlacesWithLatestPinsByMember(@Param("memberId") Long memberId, Pageable pageable);
}
