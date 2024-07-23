package sws.songpin.domain.statistics.dto.projection;

import sws.songpin.domain.genre.entity.GenreName;

import java.time.LocalDate;

public record StatsPlaceProjectionDto (
        Long placeId,
        String placeName,
        Double latitude,
        Double longitude,
        Long placePinCount,
        LocalDate listenedDate,
        Long songId,
        GenreName genreName
) {}