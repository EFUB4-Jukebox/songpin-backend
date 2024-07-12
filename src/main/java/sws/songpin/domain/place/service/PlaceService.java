package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.dto.request.PlaceAddRequestDto;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.place.dto.response.PlaceDetailsResponseDto;
import sws.songpin.domain.place.dto.response.PlaceSearchResponseDto;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;
import sws.songpin.domain.song.dto.response.SongUnitDto;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.global.BaseTimeEntity;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    // 장소 상세보기
    public PlaceDetailsResponseDto getPlaceDetails(Long placeId) {
        // 해당 Place의 Pin들을 가져와 Song끼리 grouping
        Place place = getPlaceById(placeId);
        List<Pin> pins = place.getPins();
        Map<Song, List<Pin>> pinsGroupedBySong = pins.stream()
                .collect(Collectors.groupingBy(Pin::getSong));

        // Song 리스트를 SongUnitDto 리스트로 변환
        List<SongUnitDto> songUnitDtos = pinsGroupedBySong.entrySet().stream()
                .map(entry -> SongUnitDto.from(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());

        // 해당 Place의 최신 Pin (null일 수 있음)
        Pin latestPin = pins.stream()
                .max(Comparator.comparing(BaseTimeEntity::getCreatedTime))
                .orElse(null);
        return PlaceDetailsResponseDto.from(place, latestPin, pins.size(), songUnitDtos);
    }

    // 장소 검색
    public PlaceSearchResponseDto searchPlaces(String keyword, SortBy sortBy, int pageNumber) {
        // 정렬 기준 선택
        final int pageLimit = 30; // 단일 페이지 크기
        Pageable pageable = PageRequest.of(pageNumber, pageLimit, getSortBy(sortBy));

        // 키워드의 띄어쓰기를 제거하여 띄어쓰기 없앤 장소이름을 검색
        String keywordNoSpaces = keyword.replace(" ", "");
        Page<Place> placePage;
        if (sortBy.equals(SortBy.ACCURACY)) {
            // 정확도순인 경우에는 차이 글자수 적은 순대로 정렬
            placePage = placeRepository.findAllByPlaceNameContainingIgnoreSpacesWithAccuracy(keywordNoSpaces, pageable);
        } else {
            placePage = placeRepository.findAllByPlaceNameContainingIgnoreSpaces(keywordNoSpaces, pageable);
        }
        return PlaceSearchResponseDto.from((int) placePage.getTotalElements(), placePage.getContent());
    }

    // 페이징 위해 Sort.by 설정
    private Sort getSortBy(SortBy sortBy) {
        return switch (sortBy) {
            case COUNT -> Sort.by(Sort.Order.desc("pins.size")); // `pins.size`로 정렬
            case NEWEST -> Sort.by(Sort.Order.desc("pins.createdTime")); // `pins.createdTime`으로 정렬
            case ACCURACY -> Sort.by(Sort.Order.asc("placeId")); // 정확도순인 경우에 대한 기본 정렬 설정
        };
    }

    // Place를 providerAddressId로 찾아 가져오거나 생성
    public Place getOrCreatePlace(PlaceAddRequestDto placeRequestDto) {
        return getPlaceByProviderAddressId(placeRequestDto.providerAddressId())
                .orElseGet(() -> createPlace(placeRequestDto));
    }

    public Place createPlace(PlaceAddRequestDto placeRequestDto) {
        Place place = placeRequestDto.toEntity();
        return placeRepository.save(place);
    }

    @Transactional(readOnly = true)
    public Optional<Place> getPlaceByProviderAddressId(Long providerAddressId) {
        return placeRepository.findByProviderAddressId(providerAddressId);
    }

    @Transactional(readOnly = true)
    public Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
    }
}
