package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.place.entity.Place;

public record PlaceRequestDto(
        @NotNull String placeName,
        @NotNull String address,
        @NotNull Long providerAddressId,
        @NotNull double latitude,
        @NotNull double longitude
) {
    public Place toEntity() {
        return Place.builder()
                .placeName(this.placeName())
                .address(this.address())
                .providerAddressId(this.providerAddressId())
                .latitude(this.latitude())
                .longitude(this.longitude())
                .build();
    }
}

