package sws.songpin.domain.member.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record MemberSearchResponseDto(
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        List<MemberUnitDto> memberList
) {
    public static MemberSearchResponseDto from(Page<MemberUnitDto> memberUnitDtoPage) {
        return new MemberSearchResponseDto(
                memberUnitDtoPage.getNumber(),
                memberUnitDtoPage.getSize(),
                memberUnitDtoPage.getTotalElements(),
                memberUnitDtoPage.getTotalPages(),
                memberUnitDtoPage.getContent()
        );
    }
}
