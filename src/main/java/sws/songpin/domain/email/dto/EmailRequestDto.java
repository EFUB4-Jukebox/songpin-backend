package sws.songpin.domain.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailRequestDto(
        @Email(message = "INVALID_INPUT_FORMAT-유효한 이메일 형식이 아닙니다.")
        @NotEmpty(message = "INVALID_INPUT_VALUE-이메일을 입력하세요.")
        String email
) {
}
