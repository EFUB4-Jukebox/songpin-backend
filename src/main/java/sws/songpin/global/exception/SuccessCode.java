package sws.songpin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // 도메인
    // 타입(상태 코드, "메시지");

    // Default
    SUCCESS(200, "요청 처리에 성공했습니다."),

    // Token
    LOG_IN_SUCCESS(200, "로그인에 성공했습니다."),

    // Member
    SIGN_UP_SUCCESS(200, "회원 가입에 성공했습니다."),

    ;

    private final int status;
    private final String message;
}