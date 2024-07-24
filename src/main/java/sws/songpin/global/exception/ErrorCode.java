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

    // 400 Bad Request
    // 입력 에러
    INVALID_INPUT_FORMAT(400, "유효하지 않은 형식입니다."),
    INVALID_INPUT_LENGTH(400, "입력 길이가 잘못되었습니다."),
    INVALID_INPUT_VALUE(400, "입력값이 잘못되었습니다."),
    MISSING_PARAMETER(400, "필수 파라미터가 누락되었습니다."),
    // 비밀번호, 비밀번호 확인이 서로 불일치
    PASSWORD_MISMATCH(400, "비밀번호가 일치하지 않습니다."),
    // enum 값이 잘못됨 (Visibility, GenreName, SortBy)
    INVALID_ENUM_VALUE(400, "enum 값이 잘못되었습니다."),
    // 자신과 관련해 불가능한 요청
    MEMBER_BAD_REQUEST(400, "자기 자신은 이 경로를 통해 조회할 수 없습니다."),
    FOLLOW_BAD_REQUEST(400,"자기 자신은 팔로잉할 수 없습니다."),

    // 401 Unauthorized
    // 로그인 상태여야 하는 요청
    NOT_AUTHENTICATED(401, "로그인 상태가 아닙니다."),
    // 권한이 없는 요청을 보냄
    UNAUTHORIZED_REQUEST(401,"권한이 없습니다."),
    // 로그인 시 잘못된 패스워드 입력
    LOGIN_FAIL(401, "로그인에 실패했습니다."),
    // 유효하지 않은 토큰
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    // 만료된 토큰
    EXPIRED_ACCESS_TOKEN(401,"만료된 어세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(401, "만료된 리프레시 토큰입니다."),
    // 탈퇴한 회원
    ALREADY_DELETED_MEMBER(401, "탈퇴한 회원입니다."),

    // 404 Not Found
    // 각 리소스를 찾지 못함
    MEMBER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    PLACE_NOT_FOUND(404, "장소를 찾을 수 없습니다."),
    SONG_NOT_FOUND(404, "음악을 찾을 수 없습니다."),
    PIN_NOT_FOUND(404, "핀을 찾을 수 없습니다."),
    PLAYLIST_NOT_FOUND(404, "플레이리스트를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(404, "북마크를 찾을 수 없습니다."),
    GENRE_NOT_FOUND(404, "장르를 찾을 수 없습니다."),
    FOLLOW_NOT_FOUND(404, "팔로우를 찾을 수 없습니다."),
    ALARM_NOT_FOUND(404, "알람을 찾을 수 없습니다."),
    PLAYLIST_PIN_NOT_FOUND(404, "플레이리스트 핀을 찾을 수 없습니다."),

    // 409 Conflict
    // 중복 리소스 생성 시도
    EMAIL_ALREADY_EXISTS(409, "이미 가입된 이메일입니다."),
    FOLLOW_ALREADY_EXISTS(409, "이미 팔로우하고 있습니다."),
    BOOKMARK_ALREADY_EXISTS(409, "이미 북마크가 되어있습니다."),
    PIN_ALREADY_EXISTS(409, "이미 플레이리스트에 추가된 핀입니다."),
    HANDLE_ALREADY_EXISTS(409,"이미 존재하는 핸들입니다."),

    // 500 Internal Server Error
    // 외부 API 사용 도중 에러
    REDIS_CONNECTION_ERROR(500, "서버에서 Redis 연결 중 문제가 발생했습니다."),
    EXTERNAL_API_ERROR(500, "외부 API 사용 중 문제가 발생했습니다."),

    ;

    private final int status;
    private final String message;
}