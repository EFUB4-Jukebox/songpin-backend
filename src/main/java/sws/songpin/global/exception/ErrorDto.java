package sws.songpin.global.exception;

public record ErrorDto(
        String timestamp,
        int status,
        String errorCode,
        String message,
        String path
) {}
