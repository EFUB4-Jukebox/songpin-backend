package sws.songpin.domain.playlist.dto.request;

import jakarta.validation.constraints.*;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.pin.domain.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

public record PlaylistRequestDto(
        @Size(max = 40, message = "INVALID_INPUT_LENGTH-플레이리스트 이름은 40자 이내여야 합니다.")
        String playlistName,
        Visibility visibility) {

    public Playlist toEntity(Member member) {
        return Playlist.builder()
                .playlistId(null)
                .playlistName(playlistName)
                .visibility(visibility)
                .creator(member)
                .build();
    }
}
