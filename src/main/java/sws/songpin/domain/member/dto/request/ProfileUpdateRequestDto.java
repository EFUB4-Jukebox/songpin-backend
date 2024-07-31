package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequestDto (
        @NotBlank
        String profileImg,
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "INVALID_INPUT_FORMAT-닉네임은 한글, 영문 대소문자, 숫자만 사용 가능합니다.")
        @Size(max = 8, message = "INVALID_INPUT_LENGTH-닉네임은 8자 이내여야 합니다.")
        @NotEmpty(message = "INVALID_INPUT_VALUE-닉네임을 입력하세요.")
        String nickname,
        @Pattern(regexp = "^[a-z0-9_]+$", message = "INVALID_INPUT_FORMAT-핸들은 영문 소문자, 숫자, 언더바(_)만 사용 가능합니다.")
        @Size(min = 3, max = 12, message = "INVALID_INPUT_LENGTH-핸들은 최소 3자 이상, 12자 이내여야 합니다.")
        @NotEmpty(message = "INVALID_INPUT_VALUE-핸들을 입력하세요.")
        String handle
){
}
