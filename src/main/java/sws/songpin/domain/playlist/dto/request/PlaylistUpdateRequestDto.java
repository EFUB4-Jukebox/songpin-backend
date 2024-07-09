package sws.songpin.domain.playlist.dto.request;

import jakarta.validation.constraints.Size;
import sws.songpin.domain.pin.entity.Visibility;
import sws.songpin.domain.playlist.dto.response.PlaylistPinUpdateDto;

import java.util.List;

public record PlaylistUpdateRequestDto(
        @Size(max = 40, message = "INVALID_INPUT_LENGTH-플레이리스트 이름은 40자 이내여야 합니다.")
        String playlistName,
        Visibility visibility,
        List<PlaylistPinUpdateDto> pinList) {
}
