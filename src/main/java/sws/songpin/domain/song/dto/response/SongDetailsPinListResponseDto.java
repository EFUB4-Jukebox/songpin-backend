package sws.songpin.domain.song.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record SongDetailsPinListResponseDto(
    int currentPage,
    int pageSize,
    long totalElements,
    int totalPages,
    List<SongDetailsPinDto> songDetailsPinList){

    public static SongDetailsPinListResponseDto from(Page<SongDetailsPinDto> songDetailsPage) {
        return new SongDetailsPinListResponseDto(
                songDetailsPage.getNumber(),
                songDetailsPage.getSize(),
                songDetailsPage.getTotalElements(),
                songDetailsPage.getTotalPages(),
                songDetailsPage.getContent()
        );
    }
}
