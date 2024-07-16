package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

public record MapRecentPeriodRequestDto(
        @NotNull MapBasicRequestDto mapBasicRequestDto,
        @NotNull String periodFilter // "week", "month", "threeMonths"
) {
}
