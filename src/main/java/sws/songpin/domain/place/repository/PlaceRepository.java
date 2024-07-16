package sws.songpin.domain.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.place.entity.Place;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByProviderAddressId(Long providerAddressId);

    // 좌표 범위에 포함되는 장소들 불러오기 (Pageable을 사용하여 최대 100개 결과로 제한)
    @Query(value = "SELECT p FROM Place p JOIN p.pins pin " +
            "WHERE p.latitude BETWEEN :swLat AND :neLat AND p.longitude BETWEEN :swLng AND :neLng " +
            "GROUP BY p HAVING COUNT(pin) > 0 ORDER BY MAX(pin.listenedDate) DESC",
            nativeQuery = true)
    List<Place> findAllByLatitudeBetweenAndLongitudeBetween(
            @Param("swLat") double swLat,
            @Param("neLat") double neLat,
            @Param("swLng") double swLng,
            @Param("neLng") double neLng,
            Pageable pageable
    );

    // 좌표 범위 & 기간 범위에 모두 포함되는 장소들 불러오기 (Pageable을 사용하여 최대 100개 결과로 제한)
    @Query(value = "SELECT p FROM Place p JOIN p.pins pin " +
            "WHERE p.latitude BETWEEN :swLat AND :neLat AND p.longitude BETWEEN :swLng AND :neLng AND " +
            "pin.listenedDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p HAVING COUNT(pin) > 0 ORDER BY MAX(pin.listenedDate) DESC",
            nativeQuery = true)
    List<Place> findAllByLatitudeBetweenAndLongitudeBetweenAndPinsListenedDateBetween(
            @Param("swLat") double swLat,
            @Param("neLat") double neLat,
            @Param("swLng") double swLng,
            @Param("neLng") double neLng,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    // 페이징 방식으로 장소 검색

    @Query(value = "SELECT p.place_id, p.place_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM place p " +
            "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
            "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY p.place_id " +
            "ORDER BY pin_count DESC, p.place_name ASC",
            nativeQuery = true)
    Page<Object[]> findAllByPlaceNameContainingIgnoreSpacesOrderByCount(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT p.place_id, p.place_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM place p " +
            "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
            "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY p.place_id " +
            "ORDER BY MAX(pin.created_time) DESC",
            nativeQuery = true)
    Page<Object[]> findAllByPlaceNameContainingIgnoreSpacesOrderByNewest(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT p.place_id, p.place_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM place p " +
            "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
            "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY p.place_id " +
            "ORDER BY p.place_name ASC",
            nativeQuery = true)
    Page<Object[]> findAllByPlaceNameContainingIgnoreSpacesOrderByAccuracy(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);
}