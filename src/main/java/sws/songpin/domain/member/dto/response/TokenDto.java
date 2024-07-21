package sws.songpin.domain.member.dto.response;

import lombok.Builder;

public record TokenDto(
        String accessToken,
        String refreshToken,
        int refreshTokenMaxAge
) {
    public TokenDto(String accessToken, String refreshToken){
        this(accessToken,refreshToken,7*24*60*60);
    }
}
