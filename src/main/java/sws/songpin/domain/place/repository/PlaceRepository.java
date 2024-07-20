package sws.songpin.domain.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.place.dto.response.MapPlaceUnitDto;
import sws.songpin.domain.place.entity.Place;

import java.time.LocalDate;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByProviderAddressId(Long providerAddressId);

    // 좌표 범위에 포함되는 장소들 불러오기
    @Query(value = "SELECT p.* FROM place p " +
            "JOIN pin ON p.place_id = pin.place_id " +
            "WHERE p.latitude BETWEEN :swLat AND :neLat " +
            "AND p.longitude BETWEEN :swLng AND :neLng " +
            "GROUP BY p.place_id " +
            "HAVING COUNT(pin.pin_id) > 0 " +
            "ORDER BY MAX(pin.listened_date) DESC, p.place_id DESC",
            countQuery = "SELECT COUNT(DISTINCT p.place_id) FROM place p " +
                    "JOIN pin ON p.place_id = pin.place_id " +
                    "WHERE p.latitude BETWEEN :swLat AND :neLat " +
                    "AND p.longitude BETWEEN :swLng AND :neLng " +
                    "GROUP BY p.place_id " +
                    "HAVING COUNT(pin.pin_id) > 0",
            nativeQuery = true)
    Page<Place> findAllByLatitudeBetweenAndLongitudeBetween(
            @Param("swLat") double swLat,
            @Param("neLat") double neLat,
            @Param("swLng") double swLng,
            @Param("neLng") double neLng,
            Pageable pageable
    );

    // 좌표 범위 & 기간 범위에 모두 포함되는 장소들 불러오기
    @Query(value = "SELECT p.* FROM place p " +
            "JOIN pin ON p.place_id = pin.place_id " +
            "WHERE p.latitude BETWEEN :swLat AND :neLat " +
            "AND p.longitude BETWEEN :swLng AND :neLng " +
            "AND pin.listened_date BETWEEN :startDate AND :endDate " +
            "GROUP BY p.place_id " +
            "HAVING COUNT(pin.pin_id) > 0 " +
            "ORDER BY MAX(pin.listened_date) DESC, p.place_id DESC",
            countQuery = "SELECT COUNT(DISTINCT p.place_id) FROM place p " +
                    "JOIN pin ON p.place_id = pin.place_id " +
                    "WHERE p.latitude BETWEEN :swLat AND :neLat " +
                    "AND p.longitude BETWEEN :swLng AND :neLng " +
                    "AND pin.listened_date BETWEEN :startDate AND :endDate " +
                    "GROUP BY p.place_id " +
                    "HAVING COUNT(pin.pin_id) > 0",
            nativeQuery = true)
    Page<Place> findAllByLatitudeBetweenAndLongitudeBetweenAndPinsListenedDateBetween(
            @Param("swLat") double swLat,
            @Param("neLat") double neLat,
            @Param("swLng") double swLng,
            @Param("neLng") double neLng,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    // Home 장소 검색 구현
    @Query("""
        SELECT new sws.songpin.domain.place.dto.response.MapPlaceUnitDto(
            p.placeId,
            p.latitude,
            p.longitude,
            pin.genre.genreName,
            COUNT(pin.genre)
        )
        FROM Place p
        JOIN p.pins pin
        WHERE p.latitude between :swLat AND :neLat AND p.longitude BETWEEN :swLng AND :neLng
        GROUP BY p.placeId, p.latitude, p.longitude, pin.genre
        HAVING COUNT(pin) > 0
        ORDER BY MAX(pin.listenedDate) DESC, p.placeId DESC
    """)
    Page<MapPlaceUnitDto> findPagedPlacesWithLatestPins(@Param("swLat") double swLat,
                                                        @Param("neLat") double neLat,
                                                        @Param("swLng") double swLng,
                                                        @Param("neLng") double neLng,
                                                        Pageable pageable);

    // 유저의 장소 검색 구현
    @Query("""
        SELECT new sws.songpin.domain.place.dto.response.MapPlaceUnitDto(
            p.placeId,
            p.latitude,
            p.longitude,
            MAX(pin.genre.genreName),
            COUNT(pin)
        )
        FROM Place p
        JOIN p.pins pin
        WHERE pin.member.memberId = :memberId
        GROUP BY p.placeId, p.latitude, p.longitude
        HAVING COUNT(pin) > 0
        ORDER BY MAX(pin.listenedDate) DESC, p.placeId DESC
    """)
    Page<MapPlaceUnitDto> findPagedPlacesWithLatestPinsByMember(@Param("memberId") Long memberId, Pageable pageable);


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