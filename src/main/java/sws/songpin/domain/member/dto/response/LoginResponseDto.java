package sws.songpin.domain.member.dto.response;

public record LoginResponseDto(
        String accessToken,
        String refreshToekn
) { }
