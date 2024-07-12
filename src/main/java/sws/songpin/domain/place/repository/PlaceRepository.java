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
    @Query("SELECT p FROM Place p WHERE REPLACE(p.placeName, ' ', '') LIKE %:placeNameNoSpaces%")
    Page<Place> findAllByPlaceNameContainingIgnoreSpaces(@Param("placeNameNoSpaces") String placeNameNoSpaces, Pageable pageable);
}
