package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequestDto(
        @Size(min = 8, max = 20, message = "INVALID_INPUT_LENGTH-비밀번호는 최소 8자 이상, 20자 이내여야 합니다.")
        @NotEmpty(message = "INVALID_INPUT_VALUE-비밀번호를 입력하세요.")
        String password,
        @NotEmpty(message = "INVALID_INPUT_VALUE-비밀번호 확인을 입력하세요.")
        String confirmPassword
) {
}
