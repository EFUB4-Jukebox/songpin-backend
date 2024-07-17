package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record MapBoundCoordsDto(
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") double swLat,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") double swLng,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") double neLat,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") double neLng
        /**
         * 주어진 경도와 위도에 따라 특정 영역 내의 Place 데이터를 조회하고 반환합니다.
         * swLat 남서쪽 위도
         * swLng 남서쪽 경도
         * neLat 북동쪽 위도
         * neLng 북동쪽 경도
         */
) {
}
