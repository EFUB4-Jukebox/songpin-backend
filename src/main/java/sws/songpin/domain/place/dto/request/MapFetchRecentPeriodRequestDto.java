package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

public record MapFetchRecentPeriodRequestDto(
        @NotNull MapBoundCoordsDto boundCoords,
        @NotNull String periodFilter // "week", "month", "threeMonths"
) {
}
