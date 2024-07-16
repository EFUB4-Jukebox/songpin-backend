package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

public record MapFetchBasicRequestDto(
        @NotNull MapBoundCoordsDto boundCoords
) {
}
