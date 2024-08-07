package sws.songpin.domain.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.place.entity.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByProviderAddressId(Long providerAddressId);

    //// 장소 검색 (페이징 방식)

    @Query(value = "SELECT p.place_id, p.place_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM place p " +
            "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
            "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "GROUP BY p.place_id, p.place_name " +
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
            "GROUP BY p.place_id, p.place_name " +
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
            "GROUP BY p.place_id, p.place_name " +
            "ORDER BY p.place_name ASC",
            countQuery = "SELECT COUNT(DISTINCT p.place_id) " +
                    "FROM place p " +
                    "LEFT JOIN pin pin ON p.place_id = pin.place_id " +
                    "WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces%",
            nativeQuery = true)
    Page<Object[]> findAllByPlaceNameContainingIgnoreSpacesOrderByAccuracy(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);


    // Home에서 placeId 내림차순 Top3 장소 반환을 위한 메서드

    @Query(value = """
                SELECT p.place_id, p.place_name, COUNT(DISTINCT pin.pin_id) AS place_pin_count 
                FROM place p 
                JOIN pin pin ON p.place_id = pin.place_id 
                GROUP BY p.place_id, p.place_name 
                HAVING COUNT(pin.pin_id) > 0 
                ORDER BY p.place_id DESC
                LIMIT 3 
            """, nativeQuery = true)
    List<Object[]> findTop3ByOrderByPlaceIdDesc();
}