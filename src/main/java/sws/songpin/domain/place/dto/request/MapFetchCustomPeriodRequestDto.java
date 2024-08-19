package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.genre.entity.GenreName;

import java.time.LocalDate;
import java.util.List;

public record MapFetchCustomPeriodRequestDto(
        @NotNull MapBoundCoordsDto boundCoords,
        List<GenreName> genreNameFilters,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
