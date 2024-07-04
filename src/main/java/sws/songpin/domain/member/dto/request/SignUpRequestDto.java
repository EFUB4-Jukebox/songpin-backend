package sws.songpin.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import sws.songpin.domain.member.entity.Member;

public record SignUpRequestDto(
        @Email(message = "INVALID_INPUT_FORMAT-유효한 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "INVALID_INPUT_LENGTH-이메일은 50자 이내여야 합니다.")
        String email,
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "INVALID_INPUT_FORMAT-닉네임은 한글 문자, 영어 대소문자, 숫자 조합만 허용됩니다.")
        @Size(max = 10, message = "INVALID_INPUT_LENGTH-닉네임은 10자 이내여야 합니다.")
        String nickname,
        @Size(max = 20, message = "INVALID_INPUT_LENGTH-비밀번호는 20자 이내여야 합니다.")
        String password,
        String confirmPassword) {
    public Member toEntity(String handle, String password){
        return Member.builder()
                .email(this.email)
                .nickname(this.nickname)
                .handle(handle)
                .password(password)
                .build();
    }
}