package sws.songpin.domain.playlist.dto.response;

import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

import java.time.LocalDate;
import java.util.List;

public record PlaylistDetailsResponseDto(
        Boolean isMine,
        String playlistName,
        String creatorHandle,
        String creatorNickname,
        int pinCount,
        LocalDate updatedDate,
        Visibility visibility,
        List<String> imgPathList,
        Boolean isBookmarked,
        List<PlaylistPinUnitDto> pinList) {

    public static PlaylistDetailsResponseDto from(Playlist playlist, Member creator, List<String> imgPathList, List<PlaylistPinUnitDto> pinList, Boolean isMine, Boolean isBookmarked) {
        return new PlaylistDetailsResponseDto(
                isMine,
                playlist.getPlaylistName(),
                creator.getHandle(),
                creator.getNickname(),
                pinList.size(),
                playlist.getModifiedTime().toLocalDate(),
                playlist.getVisibility(),
                imgPathList,
                isBookmarked,
                pinList
        );
    }

}
