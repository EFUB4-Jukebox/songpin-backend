package sws.songpin.domain.pin.dto.request;

import jakarta.validation.constraints.Size;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Visibility;

import java.time.LocalDate;

public record PinRequestDto(
        SongRequestDto song,
        LocalDate listenedDate,
        PlaceRequestDto place,
        GenreName genreName,
        @Size(max = 200, message = "핀의 메모 길이는 200자 이내여야 합니다.") String memo,
        Visibility visibility
) {
    public record SongRequestDto(
            String title,
            String artist,
            String imgPath,
            String providerTrackCode
    ) {}

    public record PlaceRequestDto(
            String placeName,
            String address,
            Long providerAddressId,
            double latitude,
            double longitude
    ) {}
}

