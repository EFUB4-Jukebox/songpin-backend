package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.*;
import sws.songpin.domain.member.entity.Member;

public record SignUpRequestDto(
        @Email(message = "INVALID_INPUT_FORMAT-유효한 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "INVALID_INPUT_LENGTH-이메일은 50자 이내여야 합니다.")
        String email,
        @Pattern(regexp = "^(?!.*\\s$)(?!^\\s)[가-힣a-zA-Z0-9\\s]+$", message = "INVALID_INPUT_FORMAT-닉네임은 한글 문자, 영어 대소문자, 숫자, 공백 조합만 허용됩니다. 단, 공백만으로 구성되거나 공백이 맨 앞과 맨 뒤에 올 수 없습니다.")
        @Size(max = 8, message = "INVALID_INPUT_LENGTH-닉네임은 8자 이내여야 합니다.")
        String nickname,
        @Size(min = 8, message = "INVALID_INPUT_LENGTH-비밀번호는 최소 8자 이상이어야 합니다.")
        @Size(max = 20, message = "INVALID_INPUT_LENGTH-비밀번호는 20자 이내여야 합니다.")
        @NotBlank(message = "INVALID_INPUT_VALUE-비밀번호는 한 글자 이상 입력해야 합니다.")
        String password,
        String confirmPassword) {
    public Member toEntity(String handle, String encodedPassword){
        return Member.builder()
                .email(this.email)
                .nickname(this.nickname)
                .handle(handle)
                .encodedPassword(encodedPassword)
                .build();
    }
}