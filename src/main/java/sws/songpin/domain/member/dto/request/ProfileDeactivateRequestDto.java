package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfileDeactivateRequestDto(
        @NotBlank(message = "INVALID_INPUT_VALUE-비밀번호는 한 글자 이상 입력해야 합니다.")
        String password
) {
}
