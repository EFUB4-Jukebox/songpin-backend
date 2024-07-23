package sws.songpin.domain.member.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.pin.dto.response.PinBasicUnitDto;
import sws.songpin.domain.place.dto.response.PlaceUnitDto;

import java.util.List;

public record HomeResponseDto(
        String welcomeMessage,
        List<PinBasicUnitDto> pinList,
        List<PlaceUnitDto> placeList) {

    public static HomeResponseDto from(Member member, List<PinBasicUnitDto> pinList, List<PlaceUnitDto> placeList){
        return new HomeResponseDto(
                member.getNickname()+"님,\n무슨 노래 듣고 계세요?",
                pinList,
                placeList
        );
    }
}
