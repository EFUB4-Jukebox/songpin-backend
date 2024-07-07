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
        String memo,
        LocalDate listenedDate,
        PlaceResponseDto place,
        GenreName genreName,
        Visibility visibility
) {
    public PinResponseDto(Pin pin) {
        this(
                pin.getPinId(),
                new SongResponseDto(pin.getSong()),
                pin.getMemo(),
                pin.getListenedDate(),
                new PlaceResponseDto(pin.getPlace()),
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