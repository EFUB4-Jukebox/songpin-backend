package sws.songpin.domain.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.place.dto.response.MapPlaceProjectionDto;
import sws.songpin.domain.place.entity.Place;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByProviderAddressId(Long providerAddressId);

    // 좌표 범위에 포함되는 장소들 불러오기
    @Query("""
        SELECT new sws.songpin.domain.place.dto.response.MapPlaceProjectionDto(
            p.placeId,
            p.latitude,
            p.longitude,
            COUNT(pin),
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
        AND pin.genre.genreName IN :genreNameSet
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.song.songId, latestPin.genre.genreName, latestPin.listenedDate
        HAVING COUNT(pin) > 0
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Page<MapPlaceProjectionDto> findPagedPlacesWithLatestPins(@Param("swLat") double swLat,
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
            COUNT(pin),
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
        AND pin.genre.genreName IN :genreNameSet
        AND pin.listenedDate BETWEEN :startDate AND :endDate
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.song.songId, latestPin.genre.genreName, latestPin.listenedDate
        HAVING COUNT(pin) > 0
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Page<MapPlaceProjectionDto> findPagedPlacesWithPinsListenedDateBetween(@Param("swLat") double swLat,
                                                                           @Param("neLat") double neLat,
                                                                           @Param("swLng") double swLng,
                                                                           @Param("neLng") double neLng,
                                                                           @Param("genreNameSet") Set<GenreName> genreNameSet,
                                                                           @Param("startDate") LocalDate startDate,
                                                                           @Param("endDate") LocalDate endDate,
                                                                           Pageable pageable);

    // 유저가 핀을 등록한 장소 찾기 구현
    @Query("""
        SELECT new sws.songpin.domain.place.dto.response.MapPlaceProjectionDto(
            p.placeId,
            p.latitude,
            p.longitude,
            COUNT(pin),
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
        WHERE pin.member.memberId = :memberId
        GROUP BY p.placeId, p.latitude, p.longitude, latestPin.song.songId, latestPin.genre.genreName, latestPin.listenedDate
        HAVING COUNT(pin) > 0
        ORDER BY latestPin.listenedDate DESC, p.placeId DESC
    """)
    Page<MapPlaceProjectionDto> findPagedPlacesWithLatestPinsByMember(@Param("memberId") Long memberId, Pageable pageable);


    ////
    // 페이징 방식으로 장소 검색

    @Query(value = "SELECT p.place_id, p.place_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM place p " +
            "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
            "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY p.place_id " +
            "ORDER BY pin_count DESC, p.place_name ASC",
            countQuery = "SELECT COUNT(DISTINCT p.place_id) " +
                    "FROM place p " +
                    "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
                    "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces%",
            nativeQuery = true)
    Page<Object[]> findAllByPlaceNameContainingIgnoreSpacesOrderByCount(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT p.place_id, p.place_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM place p " +
            "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
            "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY p.place_id " +
            "ORDER BY MAX(pin.created_time) DESC",
            countQuery = "SELECT COUNT(DISTINCT p.place_id) " +
                    "FROM place p " +
                    "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
                    "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces%",
            nativeQuery = true)
    Page<Object[]> findAllByPlaceNameContainingIgnoreSpacesOrderByNewest(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT p.place_id, p.place_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM place p " +
            "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
            "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY p.place_id " +
            "ORDER BY p.place_name ASC",
            countQuery = "SELECT COUNT(DISTINCT p.place_id) " +
                    "FROM place p " +
                    "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
                    "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces%",
            nativeQuery = true)
    Page<Object[]> findAllByPlaceNameContainingIgnoreSpacesOrderByAccuracy(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);
}