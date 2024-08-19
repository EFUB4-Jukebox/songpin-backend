package sws.songpin.domain.email.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sws.songpin.domain.model.ReportType;

public record ReportRequestDto(
        @NotNull Long reporterId,
        @NotNull Long reportedId,
        @NotNull ReportType reportType,
        @Size(max = 200) String reason
        ) {
}
