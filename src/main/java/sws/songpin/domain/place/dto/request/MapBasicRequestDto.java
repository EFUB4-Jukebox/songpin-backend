package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

public record MapBasicRequestDto(
        @NotNull double swLat,
        @NotNull double swLng,
        @NotNull double neLat,
        @NotNull double neLng
        /**
         * 주어진 경도와 위도에 따라 특정 영역 내의 Place 데이터를 조회하고 반환합니다.
         * swLat 남서쪽 위도
         * swLng 남서쪽 경도
         * neLat 북동쪽 위도
         * neLng 북동쪽 경도
         */
) {
}
