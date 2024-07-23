package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequestDto (
        @NotBlank
        String profileImg,
        @Pattern(regexp = "^(?!.*\\s$)(?!^\\s)[가-힣a-zA-Z0-9\\s]+$", message = "INVALID_INPUT_FORMAT-닉네임은 한글 문자, 영어 대소문자, 숫자, 공백 조합만 허용됩니다. 단, 공백만으로 구성되거나 공백이 맨 앞과 맨 뒤에 올 수 없습니다.")
        @Size(max = 8, message = "INVALID_INPUT_LENGTH-닉네임은 8자 이내여야 합니다.")
        @NotBlank
        String nickname,
        @NotBlank
        @Pattern(regexp = "^[a-z0-9_]+$", message = "INVALID_INPUT_FORMAT-핸들은 영어 소문자, 숫자, 언더바(_) 조합만 허용됩니다.")
        @Size(min = 3, message = "INVALID_INPUT_LENGTH-핸들은 최소 3자 이상이어야 합니다.")
        @Size(max = 12, message = "INVALID_INPUT_LENGTH-핸들은 12자 이내여야 합니다.")
        String handle
){
}
