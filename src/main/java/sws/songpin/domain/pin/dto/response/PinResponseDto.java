package sws.songpin.domain.pin.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.entity.Visibility;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.entity.Song;

import java.time.LocalDate;

public record PinResponseDto(
        Long pinId,
        SongResponseDto song,
        PlaceResponseDto place,
        String memo,
        LocalDate listenedDate,
        GenreName genreName,
        Visibility visibility
)  {
    public static PinResponseDto from(Pin pin) {
        return new PinResponseDto(
                pin.getPinId(),
                new SongResponseDto(pin.getSong()),
                new PlaceResponseDto(pin.getPlace()),
                pin.getMemo(),
                pin.getListenedDate(),
                pin.getGenre().getGenreName(),
                pin.getVisibility()
        );
    }

    public record SongResponseDto(
            Long songId,
            String title,
            String artist,
            String imgPath,
            String providerTrackCode
    ) {
        public SongResponseDto(Song song) {
            this(
                    song.getSongId(),
                    song.getTitle(),
                    song.getArtist(),
                    song.getImgPath(),
                    song.getProviderTrackCode()
            );
        }
    }

    public record PlaceResponseDto(
            Long placeId,
            String placeName,
            String address,
            Long providerAddressId,
            double latitude,
            double longitude
    ) {
        public PlaceResponseDto(Place place) {
            this(
                    place.getPlaceId(),
                    place.getPlaceName(),
                    place.getAddress(),
                    place.getProviderAddressId(),
                    place.getLatitude(),
                    place.getLongitude()
            );
        }

    }
}