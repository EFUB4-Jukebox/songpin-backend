package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.place.entity.PeriodFilter;

import java.util.List;

public record MapFetchRecentPeriodRequestDto(
        @NotNull MapBoundCoordsDto boundCoords,
        List<GenreName> genreNameFilters,
        @NotNull PeriodFilter periodFilter
) {
}
