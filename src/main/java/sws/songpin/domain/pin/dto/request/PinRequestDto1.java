package sws.songpin.domain.pin.dto.request;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Visibility;

import java.time.LocalDate;

// pinRequestDto로 한번에 몰아넣은 경우(현재 사용은 x)
public record PinRequestDto1(
        String title,
        String artist,
        String imgPath,
        String providerTrackCode,
        LocalDate listenedDate,
        String placeName,
        String address,
        Long providerAddressId,
        double latitude,
        double longitude,
        GenreName genreName,
        String memo,
        Visibility visibility
) {}
