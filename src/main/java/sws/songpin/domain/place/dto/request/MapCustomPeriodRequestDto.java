package sws.songpin.domain.place.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MapCustomPeriodRequestDto(
        @NotNull MapBasicRequestDto mapBasicRequestDto,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
