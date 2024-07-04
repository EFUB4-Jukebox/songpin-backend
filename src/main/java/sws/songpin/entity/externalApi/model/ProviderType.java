package sws.songpin.entity.externalApi.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProviderType {
    TEST,
    KAKAOMAP,
    SPOTIFY
    ;

    @JsonCreator
    public static ProviderType from(String s) {
        return ProviderType.valueOf(s.toUpperCase());
    }
}