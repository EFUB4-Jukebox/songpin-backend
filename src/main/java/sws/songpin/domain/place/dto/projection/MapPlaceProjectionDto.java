package sws.songpin.domain.place.dto.projection;

import sws.songpin.domain.genre.entity.GenreName;

import java.time.LocalDate;

public record MapPlaceProjectionDto (
    Long placeId,
    Double latitude,
    double longitude,
    Long placePinCount,
    LocalDate listenedDate,
    Long songId,
    GenreName genreName
) { }