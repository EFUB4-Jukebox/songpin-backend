package sws.songpin.domain.song.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record SongSearchResponseDto(
        int currentPage,
        int pageSize, // 현재 페이지 크기 (명확화 위해)
        long totalElements,
        int totalPages,
        List<SongUnitDto> songList
) {
    public static SongSearchResponseDto from(Page<SongUnitDto> songPage) {
        return new SongSearchResponseDto(
                songPage.getNumber(),
                songPage.getSize(),
                songPage.getTotalElements(),
                songPage.getTotalPages(),
                songPage.getContent()
        );
    }
}
