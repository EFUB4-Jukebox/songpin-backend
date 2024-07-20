package sws.songpin.domain.place.dto.response;

import lombok.*;
import sws.songpin.domain.genre.entity.GenreName;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MapPlaceUnitDto { // JPA DTO 직접 조회 위해 class로 생성
    private Long placeId;
    private Double latitude;
    private Double longitude;
    private GenreName genreName;
    private Long placePinCount;
}