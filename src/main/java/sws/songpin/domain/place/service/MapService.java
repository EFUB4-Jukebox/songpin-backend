package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.place.dto.request.MapBasicRequestDto;
import sws.songpin.domain.place.dto.request.MapCustomPeriodRequestDto;
import sws.songpin.domain.place.dto.request.MapRecentPeriodRequestDto;
import sws.songpin.domain.place.dto.response.MapResponseDto;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MapService {

    private final PlaceRepository placeRepository;

    public MapResponseDto getPlacesWithinBoundsByBasic(MapBasicRequestDto requestDto) {
        Pageable pageable = getCustomPageableForMap();
        return MapResponseDto.from(getPlacesWithinBounds(requestDto, pageable));
    }

    public MapResponseDto getPlaceCoordinatesByRecentPeriod(MapRecentPeriodRequestDto requestDto) {
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();
        switch (requestDto.periodFilter()) {
            case "week" -> startDate = endDate.minusWeeks(1);
            case "month" -> startDate = endDate.minusMonths(1);
            case "threeMonths"-> startDate = endDate.minusMonths(3);
            default -> throw new IllegalArgumentException("Invalid period filter: " + requestDto.periodFilter());
        }
        Pageable pageable = getCustomPageableForMap();
        List<Place> places = getPlacesWithinBoundsAndDateRange(requestDto.mapBasicRequestDto(), startDate, endDate, pageable);
        return MapResponseDto.from(places);
    }

    public MapResponseDto getPlacesWithinBoundsByCustomPeriod(MapCustomPeriodRequestDto requestDto) {
        Pageable pageable = getCustomPageableForMap();
        List<Place> places = getPlacesWithinBoundsAndDateRange(requestDto.mapBasicRequestDto(), requestDto.startDate(), requestDto.endDate(), pageable);
        return MapResponseDto.from(places);
    }

    // Map에 Place 좌표를 띄우기 위한 Custom Pageable
    private Pageable getCustomPageableForMap() {
        return PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "place_id"));
    }

    @Transactional(readOnly = true)
    public List<Place> getPlacesWithinBounds(MapBasicRequestDto requestDto, Pageable pageable) {
        return placeRepository.findAllByLatitudeBetweenAndLongitudeBetween(
                requestDto.swLat(),
                requestDto.neLat(),
                requestDto.swLng(),
                requestDto.neLat(),
                pageable
        );
    }

    @Transactional(readOnly = true)
    public List<Place> getPlacesWithinBoundsAndDateRange(MapBasicRequestDto requestDto, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return placeRepository.findAllByLatitudeBetweenAndLongitudeBetweenAndPinsListenedDateBetween(
                requestDto.swLat(),
                requestDto.neLat(),
                requestDto.swLng(),
                requestDto.neLat(),
                startDate,
                endDate,
                pageable
        );
    }

}