package sws.songpin.domain.playlistpin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.playlist.dto.request.PlaylistPinAddRequestDto;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlist.repository.PlaylistRepository;
import sws.songpin.domain.playlist.service.PlaylistService;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;


@Service
@Transactional
@RequiredArgsConstructor
public class PlaylistPinService {
    private final PlaylistService playlistService;
    private final PinService pinService;
    private final PlaylistRepository playlistRepository;
    private final MemberService memberService;

    // 플레이리스트에 핀 추가
    public void addPlaylistPin(PlaylistPinAddRequestDto requestDto) {
        Playlist playlist = playlistService.findPlaylistById(requestDto.playlistId());
        Pin pin = pinService.getPinById(requestDto.pinId());
        Member currentMember = memberService.getCurrentMember();
        // 중복 핀 체크
        boolean playlistPinExists = playlist.getPlaylistPins().stream()
                .anyMatch(playlistPin -> playlistPin.getPin().getPinId().equals(pin.getPinId()));
        if (playlistPinExists) {
            throw new CustomException(ErrorCode.PLAYLIST_PIN_ALREADY_EXISTS);
        }
        // 내 핀인지 확인
        if (!pin.getCreator().equals(currentMember)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        PlaylistPin playlistPin = createPlaylistPin(playlist, pin);
        // modifiedTime 갱신
        playlist.updatePlaylistName(playlist.getPlaylistName() + " ");
        playlistRepository.saveAndFlush(playlist);
        playlist.updatePlaylistName(playlist.getPlaylistName().trim());
        // 핀 추가
        playlist.addPlaylistPin(playlistPin);
        playlistRepository.save(playlist);
    }

    private PlaylistPin createPlaylistPin(Playlist playlist, Pin pin) {
        int pinIndex = playlist.getPlaylistPins().size();
        return PlaylistPin.builder()
                .pinIndex(pinIndex)
                .playlist(playlist)
                .pin(pin)
                .build();
    }
}
