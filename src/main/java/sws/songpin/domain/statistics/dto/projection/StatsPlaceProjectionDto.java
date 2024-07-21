package sws.songpin.domain.statistics.dto.projection;

import lombok.*;
import sws.songpin.domain.genre.entity.GenreName;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StatsPlaceProjectionDto {
    private Long placeId;
    private String placeName;
    private Double latitude;
    private Double longitude;
    private Long placePinCount;
    private LocalDate listenedDate;
    private Long songId;
    private GenreName genreName;
}