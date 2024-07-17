package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MapFetchCustomPeriodRequestDto(
        @NotNull MapBoundCoordsDto boundCoords,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
