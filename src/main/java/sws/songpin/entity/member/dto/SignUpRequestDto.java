package sws.songpin.entity.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import sws.songpin.entity.member.domain.Member;

public record SignUpRequestDto(
        @Email
        String email,
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$")
        @Size(max = 10)
        String nickname,
        @Size(min = 8, max = 20)
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