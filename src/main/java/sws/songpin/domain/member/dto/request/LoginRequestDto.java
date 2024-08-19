package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(
        @NotEmpty(message = "INVALID_INPUT_VALUE-이메일을 입력하세요.")
        String email,
        @NotEmpty(message = "INVALID_INPUT_VALUE-비밀번호를 입력하세요.")
        String password
) { }