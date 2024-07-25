package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.Email;

public record EmailRequestDto(
        @Email(message = "INVALID_INPUT_FORMAT-유효한 이메일 형식이 아닙니다.")
        String email
) {
}
