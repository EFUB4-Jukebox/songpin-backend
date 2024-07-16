package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.place.dto.request.MapBoundCoordsDto;
import sws.songpin.domain.place.dto.request.MapFetchBasicRequestDto;
import sws.songpin.domain.place.dto.request.MapFetchCustomPeriodRequestDto;
import sws.songpin.domain.place.dto.request.MapFetchRecentPeriodRequestDto;
import sws.songpin.domain.place.dto.response.MapFetchResponseDto;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional(readOnly = true) // Transaction 모두 읽기 전용
@RequiredArgsConstructor
public class MapService {

    private final PlaceRepository placeRepository;

    // 기본
    public MapFetchResponseDto getPlacesWithinBoundsByBasic(MapFetchBasicRequestDto requestDto) {
        Pageable pageable = getCustomPageableForMap();
        Page<Place> pageResult = getPlacesWithinBounds(requestDto.boundCoords(), pageable);
        return MapFetchResponseDto.from(pageResult.getContent());
    }

    // 최근 기준 기간
    public MapFetchResponseDto getPlaceCoordinatesByRecentPeriod(MapFetchRecentPeriodRequestDto requestDto) {
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();
        switch (requestDto.periodFilter()) {
            case "week" -> startDate = endDate.minusWeeks(1);
            case "month" -> startDate = endDate.minusMonths(1);
            case "threeMonths"-> startDate = endDate.minusMonths(3);
            default -> throw new IllegalArgumentException("Invalid period filter: " + requestDto.periodFilter());
        }
        Pageable pageable = getCustomPageableForMap();
        Page<Place> pageResult = getPlacePagesWithinBoundsAndDateRange(requestDto.boundCoords(), startDate, endDate, pageable);
        return MapFetchResponseDto.from(pageResult.getContent());
    }

    // 기간 직접 설정
    public MapFetchResponseDto getPlacesWithinBoundsByCustomPeriod(MapFetchCustomPeriodRequestDto requestDto) {
        Pageable pageable = getCustomPageableForMap();
        Page<Place> pageResult = getPlacePagesWithinBoundsAndDateRange(requestDto.boundCoords(), requestDto.startDate(), requestDto.endDate(), pageable);
        return MapFetchResponseDto.from(pageResult.getContent());
    }

    // Map에 Place 좌표를 띄우기 위한 Custom Pageable
    private Pageable getCustomPageableForMap() {
        return PageRequest.of(0, 100);
    }

    // 날짜 범위 조건 걸지 않는 경우
    public Page<Place> getPlacesWithinBounds(MapBoundCoordsDto dto, Pageable pageable) {
        return placeRepository.findAllByLatitudeBetweenAndLongitudeBetween(dto.swLat(), dto.neLat(), dto.swLng(), dto.neLng(), pageable);
    }

    // 날짜 범위 조건 거는 경우
    public Page<Place> getPlacePagesWithinBoundsAndDateRange(MapBoundCoordsDto dto, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return placeRepository.findAllByLatitudeBetweenAndLongitudeBetweenAndPinsListenedDateBetween(dto.swLat(), dto.neLat(), dto.swLng(), dto.neLng(), startDate, endDate, pageable);
    }
}