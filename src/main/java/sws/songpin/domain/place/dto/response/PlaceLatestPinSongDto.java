package sws.songpin.domain.place.dto.response;

import sws.songpin.domain.genre.entity.GenreName;

public record PlaceLatestPinSongDto(
        Long songId,
        GenreName avgGenreName
) {
}