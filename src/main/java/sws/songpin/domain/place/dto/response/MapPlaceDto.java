package sws.songpin.domain.place.dto.response;


import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public record MapPlaceDto(
        Long placeId,
        double placeLatitude,
        double placeLongitude,
        int placePinCount,
        Map<GenreName, Integer> placePinCountByGenre,
        PlaceLatestPinSongDto latestPinSong
) {
    // Place 객체를 PlaceDto로 변환하는 static 메서드
    public static MapPlaceDto from(Place place) {
        // Genre별 핀 갯수 생성
        Map<GenreName, Integer> placePinCountByGenreMap = place.getPins().stream()
                .map(Pin::getGenre)
                .distinct()
                .collect(Collectors.toMap(
                        genre -> genre.getGenreName(),
                        genre -> (int) place.getPins().stream().filter(pin -> pin.getGenre().equals(genre)).count()
                ));

        // 최신 핀의 정보
        PlaceLatestPinSongDto latestPinSong = place.getPins().stream()
                .max(Comparator.comparing(Pin::getCreatedTime))
                .map(pin -> new PlaceLatestPinSongDto(pin.getSong().getSongId(), pin.getSong().getAvgGenreName()))
                .orElse(null);

        return new MapPlaceDto(
                place.getPlaceId(),
                place.getLatitude(),
                place.getLongitude(),
                place.getPins().size(),
                placePinCountByGenreMap,
                latestPinSong
        );
    }
}