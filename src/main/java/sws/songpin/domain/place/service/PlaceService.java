package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.place.dto.request.PlaceAddRequestDto;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place createPlace(PlaceAddRequestDto placeRequestDto) {
        Place place = placeRequestDto.toEntity();
        return placeRepository.save(place);
    }

    @Transactional(readOnly = true)
    public Optional<Place> getPlaceByProviderAddressId(Long providerAddressId) {
        return placeRepository.findByProviderAddressId(providerAddressId);
    }

    public Place getOrCreatePlace(PlaceAddRequestDto placeRequestDto) {
        return getPlaceByProviderAddressId(placeRequestDto.providerAddressId())
                .orElseGet(() -> createPlace(placeRequestDto));
    }

}
