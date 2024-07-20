package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record MapFetchCustomPeriodRequestDto(
        @NotNull MapBoundCoordsDto boundCoords,
        List<String> genreNameFilters,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
