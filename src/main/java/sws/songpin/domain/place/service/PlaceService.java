package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.dto.request.PlaceAddRequestDto;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.place.dto.response.PlaceDetailsResponseDto;
import sws.songpin.domain.place.dto.response.PlaceSearchResponseDto;
import sws.songpin.domain.place.dto.response.PlaceUnitDto;
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

import static sws.songpin.global.common.EscapeSpecialCharactersService.escapeSpecialCharacters;

@Slf4j
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
        return PlaceDetailsResponseDto.from(place, pins.size(), latestPin, songUnitDtos);
    }

    // 장소 검색 (네이티브 쿼리 이용)
    public PlaceSearchResponseDto searchPlaces(String keyword, SortBy sortBy, Pageable pageable) {
        // 키워드의 이스케이프 처리 및 띄어쓰기 제거
        String escapedWord = escapeSpecialCharacters(keyword);
        String keywordNoSpaces = escapedWord.replace(" ", "");

        Page<Object[]> placePage;
        switch (sortBy) {
            case COUNT -> placePage = placeRepository.findAllByPlaceNameContainingIgnoreSpacesOrderByCount(keywordNoSpaces, pageable);
            case NEWEST -> placePage = placeRepository.findAllByPlaceNameContainingIgnoreSpacesOrderByNewest(keywordNoSpaces, pageable);
            case ACCURACY -> placePage = placeRepository.findAllByPlaceNameContainingIgnoreSpacesOrderByAccuracy(keywordNoSpaces, pageable);
            default -> throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }

        // Page<Object[]>를 Page<PlaceUnitDto>로 변환
        Page<PlaceUnitDto> placeUnitPage = placePage.map(objects -> {
            Long placeId = ((Number) objects[0]).longValue();
            String placeName = (String) objects[1];
            int placePinCount = ((Number) objects[2]).intValue();
            return new PlaceUnitDto(placeId, placeName, placePinCount);
        });

        // PlaceSearchResponseDto를 반환
        return PlaceSearchResponseDto.from(placeUnitPage);
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
