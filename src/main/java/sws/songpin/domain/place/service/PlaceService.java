package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }

    public Optional<Place> getPlaceByProviderAddressId(Long providerAddressId) {
        return placeRepository.findByProviderAddressId(providerAddressId);
    }
}
