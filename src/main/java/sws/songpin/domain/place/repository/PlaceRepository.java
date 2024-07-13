package sws.songpin.domain.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.place.entity.Place;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByProviderAddressId(Long providerAddressId);

    // 페이징 방식으로 장소 검색
    @Query(value = "SELECT p.* FROM place p LEFT JOIN pin pin ON p.place_id = pin.place_id WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% GROUP BY p.place_id ORDER BY COUNT(pin.pin_id) DESC, p.place_name ASC", nativeQuery = true)
    Page<Place> findAllByPlaceNameContainingIgnoreSpacesOrderByCount(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT p.* FROM place p LEFT JOIN pin pin ON p.place_id = pin.place_id WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% GROUP BY p.place_id ORDER BY MAX(pin.created_time) DESC", nativeQuery = true)
    Page<Place> findAllByPlaceNameContainingIgnoreSpacesOrderByNewest(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);

    @Query(value = "SELECT p.* FROM place p WHERE REPLACE(p.place_name, ' ', '') LIKE %:keywordNoSpaces% ORDER BY p.place_name ASC", nativeQuery = true)
    Page<Place> findAllByPlaceNameContainingIgnoreSpacesOrderByAccuracy(@Param("keywordNoSpaces") String keywordNoSpaces, Pageable pageable);
}
