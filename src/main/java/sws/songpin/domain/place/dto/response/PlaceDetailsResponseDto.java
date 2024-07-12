package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.dto.response.SongUnitDto;

import java.time.LocalDate;
import java.util.List;

public record PlaceDetailsResponseDto(
        String placeName,
        String address,
        Long providerAddressId,
        GenreName latestGenreName,
        int pinCount,
        LocalDate updatedDate,
        int songCount,
        List<SongUnitDto> songList
) {
    public static PlaceDetailsResponseDto from(Place place, Pin latestPin, int pinCount, List<SongUnitDto> songUnitDtos) {
        return new PlaceDetailsResponseDto(
                place.getPlaceName(),
                place.getAddress(),
                place.getProviderAddressId(),
                latestPin != null ? latestPin.getGenre().getGenreName() : null, // 가장 최근 핀의 장르
                pinCount,
                latestPin != null ? latestPin.getCreatedTime().toLocalDate() : null, // 가장 최근 핀의 등록 날짜
                songUnitDtos.size(),
                songUnitDtos
        );
    }
}
