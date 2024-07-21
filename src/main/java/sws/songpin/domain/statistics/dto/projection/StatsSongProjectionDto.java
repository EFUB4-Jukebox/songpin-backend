package sws.songpin.domain.statistics.dto.projection;

import lombok.*;
import sws.songpin.domain.genre.entity.GenreName;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StatsSongProjectionDto {
    private Long songId;
    private String title;
    private String artist;
    private String imgPath;
    private GenreName avgGenreName;
}
