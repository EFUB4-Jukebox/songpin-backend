package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MapFetchEntirePeriodRequestDto(
        @NotNull MapBoundCoordsDto boundCoords,
        List<String> genreNameFilters
) {
}
