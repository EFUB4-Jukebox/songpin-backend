package sws.songpin.domain.pin.dto.request;

import sws.songpin.domain.pin.entity.Visibility;

import java.time.LocalDate;

public record PinRequestDto (
        String title,
        String artist,
        String imgPath,
        LocalDate listenedDate,
        String placeName,
        String address,
        Long addressId,
        double latitude,
        double longitude,
        String genreName,
        String memo,
        String visibility
) {
    public Visibility toVisibility() {
        return Visibility.valueOf(this.visibility);
    }
}

