package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @Email(message = "INVALID_INPUT_FORMAT-유효한 이메일 형식이 아닙니다.")
        @NotBlank
        String email,
        @NotBlank
        String password
) { }