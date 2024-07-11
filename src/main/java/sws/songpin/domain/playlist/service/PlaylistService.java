package sws.songpin.domain.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.bookmark.service.BookmarkService;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.playlist.dto.request.PlaylistPinRequestDto;
import sws.songpin.domain.playlist.dto.response.*;
import sws.songpin.domain.playlist.dto.request.PlaylistRequestDto;
import sws.songpin.domain.playlist.dto.request.PlaylistUpdateRequestDto;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlist.repository.PlaylistRepository;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.playlistpin.repository.PlaylistPinRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final MemberService memberService;
    private final PinService pinService;
    private final BookmarkService bookmarkService;

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
        int pinIndex = playlist.getPlaylistPins().size();
        PlaylistPin playlistPin = PlaylistPin.builder()
                .pinIndex(pinIndex)
                .playlist(playlist)
                .pin(pin)
                .build();
        // modifiedTime 갱신
        playlist.updatePlaylistName(playlist.getPlaylistName()+" ");
        playlistRepository.saveAndFlush(playlist);
        playlist.updatePlaylistName(playlist.getPlaylistName().trim());
        // 핀 추가
        playlist.addPlaylistPin(playlistPin);
        playlistRepository.save(playlist);
    }

    // 플레이리스트 상세 정보 가져오기
    @Transactional(readOnly = true)
    public PlaylistResponseDto getPlaylist(Long playlistId) {
        Member currentMember = memberService.getCurrentMember();
        Playlist playlist = findPlaylistById(playlistId);
        List<PlaylistPin> playlistPinList = playlist.getPlaylistPins();
        // imgPathList, pinList
        List<PlaylistResponseDto.PlaylistPinListDto> pinList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();

        playlistPinList.stream()
                .sorted(Comparator.comparingInt(PlaylistPin::getPinIndex).reversed())
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
        // isMine
        boolean isMine = playlist.getCreator().equals(currentMember);
        // bookmarkId
        Long bookmarkId = bookmarkService.getBookmarkByPlaylistAndMember(playlist, currentMember)
                .map(Bookmark::getBookmarkId)
                .orElse(null);
        return PlaylistResponseDto.from(playlist, imgPathList, pinList, isMine, bookmarkId);
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
            playlist.removePlaylistPin(pin);
            playlistPinRepository.delete(pin);
        }
        playlistPinRepository.saveAll(updatedPins);
    }

    // 플레이리스트 삭제
    public void deletePlaylist(Long playlistId) {
        Playlist playlist = findPlaylistById(playlistId);
        Member currentMember = memberService.getCurrentMember();
        if(!currentMember.equals(playlist.getCreator())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        playlistRepository.delete(playlist);
    }

    // 내 플레이리스트 조회
    @Transactional(readOnly = true)
    public AllPlaylistResponseDto getAllPlaylists(){
        Member currentMember = memberService.getCurrentMember();
        return getAllPlaylists(currentMember.getMemberId());
    }

    // 유저 플레이리스트 조회
    @Transactional(readOnly = true)
    public AllPlaylistResponseDto getAllPlaylists(Long memberId) {
        Member creator = memberService.getMemberById(memberId);
        List<Playlist> playlists = playlistRepository.findAllByCreator(creator);
        List<PlaylistListDto> playlistList = playlists.stream().map(playlist -> {
            // imgPathList
            List<String> imgPathList = playlist.getPlaylistPins().stream()
                    .map(playlistPin -> playlistPin.getPin().getSong().getImgPath())
                    .limit(3)
                    .collect(Collectors.toList());
            // isBookmarked
            boolean isBookmarked = bookmarkService.getBookmarkByPlaylistAndMember(playlist, creator).isPresent();
            return new PlaylistListDto(
                    playlist.getPlaylistId(),
                    playlist.getPlaylistName(),
                    playlist.getCreator().getNickname(),
                    playlist.getPlaylistPins().size(),
                    playlist.getModifiedTime(),
                    playlist.getVisibility(),
                    imgPathList,
                    isBookmarked
            );
        }).collect(Collectors.toList());
        return AllPlaylistResponseDto.from(playlistList);
    }

    // 북마크된 플레이리스트 조회
    @Transactional(readOnly = true)
    public AllBookmarkResponseDto getAllBookmarks() {
        Member currentMember = memberService.getCurrentMember();
        List<Bookmark> bookmarks = bookmarkService.getBookmarksByMember(currentMember);
        List<PlaylistListDto> bookmarkList = bookmarks.stream().map(bookmark -> {
            Playlist playlist = bookmark.getPlaylist();
            // imgPathList
            List<String> imgPathList = playlist.getPlaylistPins().stream()
                    .map(playlistPin -> playlistPin.getPin().getSong().getImgPath())
                    .limit(3)
                    .collect(Collectors.toList());
            // isBookmarked
            boolean isBookmarked = true;
            return new PlaylistListDto(
                    playlist.getPlaylistId(),
                    playlist.getPlaylistName(),
                    playlist.getCreator().getNickname(),
                    playlist.getPlaylistPins().size(),
                    playlist.getModifiedTime(),
                    playlist.getVisibility(),
                    imgPathList,
                    isBookmarked
            );
        }).collect(Collectors.toList());
        return AllBookmarkResponseDto.from(bookmarkList);
    }
}
