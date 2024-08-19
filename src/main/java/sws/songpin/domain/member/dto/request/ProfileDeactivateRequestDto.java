package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ProfileDeactivateRequestDto(
        @NotEmpty(message = "INVALID_INPUT_VALUE-비밀번호를 입력하세요.")
        String password
) {
}
