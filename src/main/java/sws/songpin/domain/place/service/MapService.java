package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.place.dto.request.MapBoundCoordsDto;
import sws.songpin.domain.place.dto.request.MapFetchEntirePeriodRequestDto;
import sws.songpin.domain.place.dto.request.MapFetchCustomPeriodRequestDto;
import sws.songpin.domain.place.dto.request.MapFetchRecentPeriodRequestDto;
import sws.songpin.domain.place.dto.response.MapFetchResponseDto;
import sws.songpin.domain.place.dto.response.MapPlaceProjectionDto;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true) // Transaction 모두 읽기 전용
@RequiredArgsConstructor
public class MapService {

    private final PlaceRepository placeRepository;

    // 장르 필터링
    // 장소 좌표들 가져오기-전체 기간
    public MapFetchResponseDto getMapPlacesWithinBoundsByEntirePeriod(MapFetchEntirePeriodRequestDto reqDto) {
        Set<GenreName> genreNameSet =  getSelectedGenreNames(reqDto.genreNameFilters());
        Page<MapPlaceProjectionDto> dtoPage = getMapPlacePagesWithinBounds(reqDto.boundCoords(), genreNameSet);
        return MapFetchResponseDto.from(dtoPage);
    }

    // 장소 좌표들 가져오기-최근 기준 기간
    public MapFetchResponseDto getMapPlacesWithinBoundsByRecentPeriod(MapFetchRecentPeriodRequestDto reqDto) {
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();
        switch (reqDto.periodFilter()) {
            case "week" -> startDate = endDate.minusWeeks(1);
            case "month" -> startDate = endDate.minusMonths(1);
            case "threeMonths"-> startDate = endDate.minusMonths(3);
            default -> throw new IllegalArgumentException("Invalid period filter: " + reqDto.periodFilter());
        }
        Set<GenreName> genreNameSet =  getSelectedGenreNames(reqDto.genreNameFilters());
        Page<MapPlaceProjectionDto> dtoPage = getMapPlacePagesWithinBoundsAndDateRange(reqDto.boundCoords(), genreNameSet, startDate, endDate);
        return MapFetchResponseDto.from(dtoPage);
    }

    // 장소 좌표들 가져오기-기간 직접 설정
    public MapFetchResponseDto getMapPlacesWithinBoundsByCustomPeriod(MapFetchCustomPeriodRequestDto reqDto) {
        Set<GenreName> genreNameSet =  getSelectedGenreNames(reqDto.genreNameFilters());
        Page<MapPlaceProjectionDto> dtoPage = getMapPlacePagesWithinBoundsAndDateRange(reqDto.boundCoords(), genreNameSet, reqDto.startDate(), reqDto.endDate());
        return MapFetchResponseDto.from(dtoPage);
    }

    // 날짜 범위 조건 걸지 않는 경우
    public Page<MapPlaceProjectionDto> getMapPlacePagesWithinBounds(MapBoundCoordsDto dto, Set<GenreName> genreNameSet) {
        Pageable pageable = getCustomPageableForMap();
        return placeRepository.findPagedPlacesWithLatestPins(dto.swLat(), dto.neLat(), dto.swLng(), dto.neLng(), genreNameSet, pageable);
    }

    // 날짜 범위 조건 거는 경우
    public Page<MapPlaceProjectionDto> getMapPlacePagesWithinBoundsAndDateRange(MapBoundCoordsDto dto, Set<GenreName> genreNameSet, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = getCustomPageableForMap();
        return placeRepository.findPagedPlacesWithPinsListenedDateBetween(dto.swLat(), dto.neLat(), dto.swLng(), dto.neLng(), genreNameSet, startDate, endDate, pageable);
    }

    // 장르 필터링에 포함할 리스트 생성
    private Set<GenreName> getSelectedGenreNames(List<String> genreNameFilters) {
        if (genreNameFilters == null || genreNameFilters.isEmpty()) {
            // 모든 GenreName 값을 포함하는 Set 생성
            return new HashSet<>(Set.of(GenreName.values()));
        } else {
            // List<String>을 GenreName으로 변환하여 HashSet 생성 (순서 중요x)
            return genreNameFilters.stream()
                    .map(GenreName::from)
                    .collect(Collectors.toCollection(HashSet::new));
        }
    }

    // 지도에 장소 좌표를 최대 100개 띄우도록 함
    private Pageable getCustomPageableForMap() {
        return PageRequest.of(0, 100);
    }

    //// 유저로 필터링
    // 유저가 핀을 등록한 장소 좌표들 가져오기
    public MapFetchResponseDto getMapPlacesOfMember(Long memberId) {
        Pageable pageable = PageRequest.of(0, 300);
        Page<MapPlaceProjectionDto> dtoPage = placeRepository.findPagedPlacesWithLatestPinsByMember(memberId, pageable);
        return MapFetchResponseDto.from(dtoPage);
    }
}