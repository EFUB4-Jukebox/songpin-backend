package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequestDto(
        @NotBlank
        String uuid,
        @Size(min = 8, max = 20, message = "INVALID_INPUT_LENGTH-비밀번호는 최소 8자 이상, 20자 이내만 허용됩니다.")
        @NotBlank(message = "INVALID_INPUT_VALUE-비밀번호는 한 글자 이상 입력해야 합니다.")
        String password,
        String confirmPassword
) {
}
