package sws.songpin.domain.place.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.place.dto.projection.MapPlaceProjectionDto;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.statistics.dto.projection.StatsPlaceProjectionDto;

import java.time.LocalDate;
import java.util.List;

public interface MapPlaceRepository extends JpaRepository<Place, Long> {

    //// Home 페이지
    // 좌표 범위에 포함되는 장소들 불러오기
    @Query("""
        SELECT new sws.songpin.domain.place.dto.projection.MapPlaceProjectionDto(
            p.placeId, p.latitude, p.longitude, COUNT(pin), latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin ON pin.genre.genreName IN :genreNameList
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.genre.genreName IN :genreNameList AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p
            AND innerPin.genre.genreName IN :genreNameList
        )
        WHERE p.latitude BETWEEN :swLat AND :neLat
        AND p.longitude BETWEEN :swLng AND :neLng
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<MapPlaceProjectionDto> findPlacesWithLatestPinsByGenre(@Param("swLat") double swLat,
                                                                 @Param("neLat") double neLat,
                                                                 @Param("swLng") double swLng,
                                                                 @Param("neLng") double neLng,
                                                                 @Param("genreNameList") List<GenreName> genreNameList,
                                                                 Pageable pageable);

    // 좌표 범위 & 기간 범위에 모두 포함되는 장소들 불러오기
    @Query("""
        SELECT new sws.songpin.domain.place.dto.projection.MapPlaceProjectionDto(
            p.placeId, p.latitude, p.longitude, COUNT(pin), latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin ON pin.genre.genreName IN :genreNameList AND pin.listenedDate BETWEEN :startDate AND :endDate
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.genre.genreName IN :genreNameList AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p
            AND innerPin.genre.genreName IN :genreNameList
        )
        WHERE p.latitude BETWEEN :swLat AND :neLat
        AND p.longitude BETWEEN :swLng AND :neLng
        AND pin.listenedDate BETWEEN :startDate AND :endDate
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<MapPlaceProjectionDto> findPlacesWithLatestPinsByGenreAndDateRange(@Param("swLat") double swLat,
                                                                             @Param("neLat") double neLat,
                                                                             @Param("swLng") double swLng,
                                                                             @Param("neLng") double neLng,
                                                                             @Param("genreNameList") List<GenreName> genreNameList,
                                                                             @Param("startDate") LocalDate startDate,
                                                                             @Param("endDate") LocalDate endDate,
                                                                             Pageable pageable);

    //// Member 페이지
    // 유저가 핀을 등록한 지도 장소들 불러오기
    @Query("""
        SELECT new sws.songpin.domain.place.dto.projection.MapPlaceProjectionDto(
            p.placeId, p.latitude, p.longitude, COUNT(pin), latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin ON pin.member.memberId = :memberId
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.member.memberId = :memberId AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p AND innerPin.member.memberId = :memberId
        )
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<MapPlaceProjectionDto> findPlacesWithLatestPinsByMember(Long memberId, Pageable pageable);

    //// 통계 페이지
    // 모든 장르 통틀어 가장 핀이 많이 등록된 장소 가져오기
    @Query("""
        SELECT new sws.songpin.domain.statistics.dto.projection.StatsPlaceProjectionDto(
            p.placeId, p.placeName, p.latitude, p.longitude, COUNT(pin), latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p
        )
        GROUP BY p.placeId, p.placeName, p.latitude, p.longitude, latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        ORDER BY COUNT(pin) DESC, latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<StatsPlaceProjectionDto> findTopPlaces(Pageable pageable);

    // 해당 장르에서 가장 핀이 많이 등록된 장소 가져오기
    @Query("""
        SELECT new sws.songpin.domain.statistics.dto.projection.StatsPlaceProjectionDto(
            p.placeId, p.placeName, p.latitude, p.longitude, COUNT(pin), latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        )
        FROM Place p
        JOIN p.pins pin ON pin.genre.genreName = :genreName
        LEFT JOIN Pin latestPin ON latestPin.place = p AND latestPin.genre.genreName = :genreName AND latestPin.listenedDate = (
            SELECT MAX(innerPin.listenedDate)
            FROM Pin innerPin
            WHERE innerPin.place = p  AND latestPin.genre.genreName = :genreName
        )
        GROUP BY p.placeId, p.placeName, p.latitude, p.longitude, latestPin.listenedDate, latestPin.song.songId, latestPin.genre.genreName
        ORDER BY COUNT(pin) DESC, latestPin.listenedDate DESC, p.placeId DESC
    """)
    Slice<StatsPlaceProjectionDto> findTopPlacesByGenreName(@Param("genreName") GenreName genreName, Pageable pageable);
}