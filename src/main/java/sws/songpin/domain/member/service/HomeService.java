package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.dto.response.HomeResponseDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.pin.dto.response.PinBasicUnitDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.dto.response.PlaceUnitDto;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeService {
    private final MemberService memberService;
    private final PinRepository pinRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public HomeResponseDto getHome() {
        Member currentMember = memberService.getCurrentMember();
        // pinList
        List<Pin> pins = pinRepository.findTop3ByOrderByPinIdDesc();
        List<PinBasicUnitDto> pinBasicUnitDtos = pins.stream()
                .map(pin -> PinBasicUnitDto.from(pin, pin.getCreator().equals(currentMember)))
                .collect(Collectors.toList());
        // placeList
        List<Object[]> placesResult = placeRepository.findTop3NewestPlacesForHome(PageRequest.of(0, 3)).getContent();
        List<PlaceUnitDto> placeUnitDtos = placesResult.stream()
                .map(PlaceUnitDto::from)
                .collect(Collectors.toList());
        return HomeResponseDto.from(currentMember, pinBasicUnitDtos, placeUnitDtos);
    }
}
