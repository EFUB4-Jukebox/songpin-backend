package sws.songpin.domain.playlist.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PlaylistSearchResponseDto(
        int currentPage,
        int pageSize, // 현재 페이지 크기
        long totalElements,
        int totalPages,
        long playlistCount,
        List<PlaylistUnitDto> playlists) {

    public static PlaylistSearchResponseDto from(long playlistCount, Page<PlaylistUnitDto> playlistUnitPage) {
        return new PlaylistSearchResponseDto(
                playlistUnitPage.getNumber(),
                playlistUnitPage.getSize(),
                playlistUnitPage.getTotalElements(),
                playlistUnitPage.getTotalPages(),
                playlistCount,
                playlistUnitPage.getContent()
        );
    }
}
