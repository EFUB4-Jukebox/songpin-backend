package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequestDto (
        @NotBlank
        String profileImg,
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "INVALID_INPUT_FORMAT-닉네임은 한글 문자, 영어 대소문자, 숫자만 허용됩니다. 공백은 허용되지 않습니다.")
        @Size(max = 8, message = "INVALID_INPUT_LENGTH-닉네임은 8자 이내여야 합니다.")
        @NotBlank
        String nickname,
        @NotBlank
        @Pattern(regexp = "^[a-z0-9_]+$", message = "INVALID_INPUT_FORMAT-핸들은 영어 소문자, 숫자, 언더바(_) 조합만 허용됩니다.")
        @Size(min = 3, max = 12, message = "INVALID_INPUT_LENGTH-핸들은 최소 3자 이상, 12자 이내만 허용됩니다.")
        String handle
){
}
