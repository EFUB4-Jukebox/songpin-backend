package sws.songpin.domain.song.dto.response;

public record SpotifySearchDto(
        String title,
        String artist,
        String imgPath,
        String providerTrackCode
) {}
