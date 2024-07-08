package sws.songpin.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.place.entity.Place;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findAllByPlaceNameContaining(String placeName);
    Optional<Place> findByProviderAddressId(Long providerAddressId);
}
