package sws.songpin.domain.statistics.dto.projection;

import sws.songpin.domain.genre.entity.GenreName;

public record StatsSongProjectionDto (
    Long songId,
    String title,
    String artist,
    String imgPath,
    GenreName avgGenreName
) {}