package sws.songpin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 도메인
    // 타입(상태 코드, "메시지");

    // Default
    ERROR(400, "요청 처리에 실패했습니다."),
    ENTITY_NOT_FOUND(404, "해당 엔티티를 찾을 수 없습니다."),

    // Member
    MEMBER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXIST(409, "이미 가입된 이메일입니다."),
    PASSWORD_MISMATCH(400,"비밀번호가 일치하지 않습니다."),

    ;

    private final int status;
    private final String message;
}