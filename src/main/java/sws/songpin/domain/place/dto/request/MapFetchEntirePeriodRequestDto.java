package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;
import sws.songpin.domain.genre.entity.GenreName;

import java.util.List;

public record MapFetchEntirePeriodRequestDto(
        @NotNull MapBoundCoordsDto boundCoords,
        List<GenreName> genreNameFilters
) {
}
