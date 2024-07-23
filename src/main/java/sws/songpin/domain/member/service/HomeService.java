package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.dto.response.HomeResponseDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.repository.MemberRepository;
import sws.songpin.domain.pin.dto.response.PinBasicUnitDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.dto.response.PlaceUnitDto;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PinRepository pinRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public HomeResponseDto getHome() {
        Member currentMember = memberService.getCurrentMember();
        List<Pin> pins = pinRepository.findTop3ByOrderByPinIdDesc();
        List<Place> places = placeRepository.findTop3ByOrderByPlaceIdDesc();
        // pinList
        List<PinBasicUnitDto> pinList = pins.stream()
                .map(pin -> PinBasicUnitDto.from(pin, pin.getMember().equals(currentMember)))
                .collect(Collectors.toList());
        // placeList
        List<PlaceUnitDto> placeList = places.stream()
                .map(place -> {
                    int placePinCount = place.getPins().size();
                    return PlaceUnitDto.from(place, placePinCount);
                })
                .collect(Collectors.toList());
        return HomeResponseDto.from(currentMember, pinList, placeList);
    }
}
