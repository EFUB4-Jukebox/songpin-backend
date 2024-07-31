package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(
        @Email(message = "INVALID_INPUT_FORMAT-유효한 이메일 형식이 아닙니다.")
        @NotEmpty(message = "INVALID_INPUT_VALUE-이메일을 입력하세요.")
        String email,
        @NotEmpty(message = "INVALID_INPUT_VALUE-비밀번호를 입력하세요.")
        String password
) { }