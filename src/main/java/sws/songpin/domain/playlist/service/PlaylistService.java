package sws.songpin.domain.playlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.pin.domain.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.playlist.dto.response.PlaylistPinUpdateDto;
import sws.songpin.domain.playlist.dto.request.PlaylistRequestDto;
import sws.songpin.domain.playlist.dto.request.PlaylistUpdateRequestDto;
import sws.songpin.domain.playlist.dto.response.PlaylistResponseDto;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlist.repository.PlaylistRepository;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.playlistpin.repository.PlaylistPinRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final PinRepository pinRepository;
    private final MemberService memberService;

    // 플레이리스트 생성
    public Long createPlaylist(PlaylistRequestDto requestDto) {
        Member member = memberService.getCurrentMember();
        Playlist playlist = requestDto.toEntity(member);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return savedPlaylist.getPlaylistId();
    }

    @Transactional(readOnly = true)
    public Playlist findPlaylistById(Long playlistId){
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    // 플레이리스트에 핀 추가
    public void addPlaylistPin(Playlist playlist, Pin pin) {
        int pinIndex = playlist.getPinCount()+1;
        playlist.setPinCount(pinIndex);

        PlaylistPin playlistPin = PlaylistPin.builder()
                .pinIndex(pinIndex)
                .playlist(playlist)
                .pin(pin)
                .build();
        playlistPinRepository.save(playlistPin);
    }

    // 플레이리스트 상세 정보 가져오기
    @Transactional(readOnly = true)
    public PlaylistResponseDto getPlaylist(Long playlistId) {
        Playlist playlist = findPlaylistById(playlistId);
        List<PlaylistPin> playlistPinList = playlist.getPlaylistPins();
        // imgPathList
        List<String> imgPathList = playlistPinList.stream()
                .sorted(Comparator.comparingInt(PlaylistPin::getPinIndex))
                .limit(4)
                .map(playlistPin -> playlistPin.getPin().getSong().getImgPath())
                .collect(Collectors.toList());
        // pinList
        List<PlaylistResponseDto.PlaylistPinListDto> pinList = playlistPinList.stream()
                .map(playlistPin -> new PlaylistResponseDto.PlaylistPinListDto(
                        playlistPin.getPlaylistPinId(),
                        playlistPin.getPin().getPinId(),
                        // SongInfo
                        new PlaylistResponseDto.SongInfo(
                                playlistPin.getPin().getSong().getSongId(),
                                playlistPin.getPin().getSong().getTitle(),
                                playlistPin.getPin().getSong().getArtist(),
                                playlistPin.getPin().getSong().getImgPath()
                        ),
                        playlistPin.getPin().getListenedDate(),
                        playlistPin.getPin().getPlace().getPlaceName(),
                        playlistPin.getPin().getPlace().getProviderAddressId(),
                        playlistPin.getPin().getGenre().getGenreName(),
                        playlistPin.getPinIndex()
                ))
                .collect(Collectors.toList());
        return PlaylistResponseDto.fromEntity(playlist, imgPathList, pinList);
    }

    // 플레이리스트 편집
    public void updatePlaylist(Long playlistId, PlaylistUpdateRequestDto requestDto) {
        Playlist playlist = findPlaylistById(playlistId);
        // 플레이리스트 정보 수정
        playlist.updatePlaylistName(requestDto.playlistName());
        playlist.updateVisibility(requestDto.visibility());

        // 현재 핀 리스트 가져오기
        List<PlaylistPin> currentPins = playlist.getPlaylistPins();

        // 핀 삭제 처리
        List<Long> requestedPinIds = requestDto.pinList().stream()
                .map(PlaylistPinUpdateDto::playlistPinId)
                .collect(Collectors.toList());
        List<PlaylistPin> remainingPins = currentPins.stream()
                .filter(pin -> {
                    if (!requestedPinIds.contains(pin.getPlaylistPinId())) {
                        playlistPinRepository.delete(pin);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        // 인덱스 재조정
        for (int i = 0; i < remainingPins.size(); i++) {
            PlaylistPin playlistPin = remainingPins.get(i);
            playlistPin.updatePinIndex(i);
            playlistPinRepository.save(remainingPins.get(i));
        }

        // 핀 순서 변경
        for (PlaylistPinUpdateDto pinDto : requestDto.pinList()) {
            PlaylistPin playlistPin = currentPins.stream()
                    .filter(pin -> pin.getPlaylistPinId().equals(pinDto.playlistPinId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_PIN_NOT_FOUND));
            playlistPin.updatePinIndex(pinDto.pinIndex());
            playlistPinRepository.save(playlistPin);
        }
    }

    // 플레이리스트 삭제
    public void deletePlaylist(Long playlistId) {
        Playlist playlist = findPlaylistById(playlistId);
        Member currentmember = memberService.getCurrentMember();
        if(!currentmember.equals(playlist.getCreator())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        playlistRepository.delete(playlist);
    }
}
