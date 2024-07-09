package sws.songpin.domain.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.playlist.dto.request.PlaylistPinRequestDto;
import sws.songpin.domain.playlist.dto.response.PlaylistCreateResponseDto;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final PinRepository pinRepository;
    private final MemberService memberService;
    private final PinService pinService;

    // 플레이리스트 생성
    public PlaylistCreateResponseDto createPlaylist(PlaylistRequestDto requestDto) {
        Member member = memberService.getCurrentMember();
        Playlist playlist = requestDto.toEntity(member);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return PlaylistCreateResponseDto.from(savedPlaylist);
    }

    @Transactional(readOnly = true)
    public Playlist findPlaylistById(Long playlistId){
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    // 플레이리스트에 핀 추가
    public void addPlaylistPin(PlaylistPinRequestDto requestDto) {
        Playlist playlist = findPlaylistById(requestDto.playlistId());
        Pin pin = pinService.getPinById(requestDto.pinId())
                .orElseThrow(() -> new CustomException(ErrorCode.PIN_NOT_FOUND));
        // 중복 핀 체크
        boolean pinExists = playlist.getPlaylistPins().stream()
                .anyMatch(playlistPin -> playlistPin.getPin().getPinId().equals(pin.getPinId()));
        if (pinExists) {
            throw new CustomException(ErrorCode.PIN_ALREADY_EXISTS);
        }
        int pinIndex = playlist.getPlaylistPins().size() + 1;
        PlaylistPin playlistPin = PlaylistPin.builder()
                .pinIndex(pinIndex)
                .playlist(playlist)
                .pin(pin)
                .build();
//        playlistPinRepository.save(playlistPin);
        List<PlaylistPin> newPlaylistPins = new ArrayList<>();
        newPlaylistPins.add(playlistPin);
        playlistPinRepository.saveAll(newPlaylistPins);
    }

    // 플레이리스트 상세 정보 가져오기
    @Transactional(readOnly = true)
    public PlaylistResponseDto getPlaylist(Long playlistId) {
        Playlist playlist = findPlaylistById(playlistId);
        List<PlaylistPin> playlistPinList = playlist.getPlaylistPins();
        // imgPathList, pinList
        List<PlaylistResponseDto.PlaylistPinListDto> pinList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();

        playlistPinList.stream()
                .sorted(Comparator.comparingInt(PlaylistPin::getPinIndex))
                .forEach(playlistPin -> {
                    // SongInfo
                    PlaylistResponseDto.SongInfo songInfo = new PlaylistResponseDto.SongInfo(
                            playlistPin.getPin().getSong().getSongId(),
                            playlistPin.getPin().getSong().getTitle(),
                            playlistPin.getPin().getSong().getArtist(),
                            playlistPin.getPin().getSong().getImgPath()
                    );
                    // PlaylistPinListDto
                    PlaylistResponseDto.PlaylistPinListDto pinListDto = new PlaylistResponseDto.PlaylistPinListDto(
                            playlistPin.getPlaylistPinId(),
                            playlistPin.getPin().getPinId(),
                            songInfo,
                            playlistPin.getPin().getListenedDate(),
                            playlistPin.getPin().getPlace().getPlaceName(),
                            playlistPin.getPin().getPlace().getProviderAddressId(),
                            playlistPin.getPin().getGenre().getGenreName(),
                            playlistPin.getPinIndex()
                    );
                    pinList.add(pinListDto);

                    if (imgPathList.size() < 3) {
                        imgPathList.add(playlistPin.getPin().getSong().getImgPath());
                    }
                });
        return PlaylistResponseDto.from(playlist, imgPathList, pinList);
    }

    // 플레이리스트 편집
    public void updatePlaylist(Long playlistId, PlaylistUpdateRequestDto requestDto) {
        Playlist playlist = findPlaylistById(playlistId);
        // 플레이리스트 정보 수정
        playlist.updatePlaylistName(requestDto.playlistName());
        playlist.updateVisibility(requestDto.visibility());

        // 현재 핀 리스트 가져오기
        List<PlaylistPin> currentPins = playlist.getPlaylistPins();

        // 요청된 핀 리스트 가져오기
        List<Long> requestedPinIds = requestDto.pinList().stream()
                .map(PlaylistPinUpdateDto::playlistPinId)
                .collect(Collectors.toList());

        // 핀 삭제 & 순서 변경
        List<PlaylistPin> updatedPins = new ArrayList<>();
        List<PlaylistPin> pinsToDelete = new ArrayList<>();
        for (PlaylistPin currentPin : currentPins) {
            // 핀 삭제
            if (!requestedPinIds.contains(currentPin.getPlaylistPinId())) {
                pinsToDelete.add(currentPin);
            } else {
                // 핀 순서 변경
                PlaylistPinUpdateDto pinDto = requestDto.pinList().stream()
                        .filter(dto -> dto.playlistPinId().equals(currentPin.getPlaylistPinId()))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_PIN_NOT_FOUND));
                currentPin.updatePinIndex(pinDto.pinIndex());
                updatedPins.add(currentPin);
            }
        }
        for (PlaylistPin pin : pinsToDelete) {
            log.info("핀 삭제: " + pin.getPlaylistPinId());
            playlist.removePlaylistPin(pin);
            playlistPinRepository.delete(pin);
        }
        log.info("핀 업데이트: " + updatedPins);
        playlistPinRepository.saveAll(updatedPins);

//        // 핀 삭제 처리
//        List<Long> requestedPinIds = requestDto.pinList().stream()
//                .map(PlaylistPinUpdateDto::playlistPinId)
//                .collect(Collectors.toList());
//        List<PlaylistPin> remainingPins = currentPins.stream()
//                .filter(pin -> {
//                    if (!requestedPinIds.contains(pin.getPlaylistPinId())) {
//                        playlistPinRepository.delete(pin);
//                        return false;
//                    }
//                    return true;
//                })
//                .collect(Collectors.toList());
//
//        // 인덱스 재조정
//        for (int i = 0; i < remainingPins.size(); i++) {
//            PlaylistPin playlistPin = remainingPins.get(i);
//            playlistPin.updatePinIndex(i);
//            playlistPinRepository.save(remainingPins.get(i));
//        }
//
//        // 핀 순서 변경
//        for (PlaylistPinUpdateDto pinDto : requestDto.pinList()) {
//            PlaylistPin playlistPin = currentPins.stream()
//                    .filter(pin -> pin.getPlaylistPinId().equals(pinDto.playlistPinId()))
//                    .findFirst()
//                    .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_PIN_NOT_FOUND));
//            playlistPin.updatePinIndex(pinDto.pinIndex());
//            playlistPinRepository.save(playlistPin);
//        }
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
