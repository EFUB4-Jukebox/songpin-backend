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
        double latitude,
        double longitude,
        int placePinCount,
        GenreName latestGenreName,
        LocalDate updatedDate,
        List<SongUnitDto> songList
) {
    public static PlaceDetailsResponseDto from(Place place, int placePinCount, Pin latestPin, List<SongUnitDto> songUnitDtos) {
        return new PlaceDetailsResponseDto(
                place.getPlaceName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                placePinCount,
                latestPin != null ? latestPin.getGenre().getGenreName() : null, // 가장 최근 핀의 장르
                latestPin != null ? latestPin.getCreatedTime().toLocalDate() : null, // 가장 최근 핀의 등록 날짜
                songUnitDtos
        );
    }
}
