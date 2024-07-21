package sws.songpin.domain.statistics.dto.response;

public record StatsPopularSongDto(
        String title,
        String artist,
        String imgPath
) {
    public static StatsPopularSongDto from(StatsSongProjectionDto dto) {
        return new StatsPopularSongDto(dto.getTitle(), dto.getArtist(), dto.getImgPath());
    }
}