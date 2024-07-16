package sws.songpin.domain.pin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.genre.service.GenreService;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.dto.request.PinAddRequestDto;
import sws.songpin.domain.pin.dto.request.PinUpdateRequestDto;
import sws.songpin.domain.pin.dto.response.FeedPinListResponseDto;
import sws.songpin.domain.pin.dto.response.FeedPinUnitDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.service.PlaceService;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.playlistpin.repository.PlaylistPinRepository;
import sws.songpin.domain.song.dto.response.SongDetailsPinDto;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.service.SongService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final SongRepository songRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final MemberService memberService;
    private final SongService songService;
    private final PlaceService placeService;
    private final GenreService genreService;

    // 음악 핀 생성 - 노래, 장소가 없다면 추가하기
    public Long createPin(PinAddRequestDto pinAddRequestDto) {
        Member member = memberService.getCurrentMember();
        Song finalSong = songService.getOrCreateSong(pinAddRequestDto.song());
        Place finalPlace = placeService.getOrCreatePlace(pinAddRequestDto.place());
        Genre genre = genreService.getGenreByGenreName(pinAddRequestDto.genreName());

        Pin pin = Pin.builder()
                .listenedDate(pinAddRequestDto.listenedDate())
                .memo(pinAddRequestDto.memo())
                .visibility(pinAddRequestDto.visibility())
                .member(member)
                .song(finalSong)
                .place(finalPlace)
                .genre(genre)
                .build();
        pinRepository.save(pin);
        updateSongAvgGenreName(finalSong);

        // 노래 상세정보 페이지로 이동
        return finalSong.getSongId();
    }

    // 음악 핀 수정
    public Long updatePin(Long pinId, PinUpdateRequestDto pinUpdateRequestDto) {
        Pin pin = validatePinCreator(pinId);

        Genre genre = genreService.getGenreByGenreName(pinUpdateRequestDto.genreName());
        pin.updatePin(pinUpdateRequestDto.listenedDate(), pinUpdateRequestDto.memo(), pinUpdateRequestDto.visibility(), genre);
        pinRepository.save(pin);

        return pin.getSong().getSongId();
    }

    // 음악 핀 삭제
    public void deletePin(Long pinId) {
        Pin pin = validatePinCreator(pinId);
        List<PlaylistPin> playlistPins = playlistPinRepository.findAllByPin(pin);
        for (PlaylistPin playlistPin : playlistPins) {
            Playlist playlist = playlistPin.getPlaylist();
            playlist.removePlaylistPin(playlistPin);
        }
        playlistPinRepository.deleteAll(playlistPins);
        pinRepository.delete(pin);
    }

    private void updateSongAvgGenreName(Song song) {
        List<Genre> genres = pinRepository.findAllBySong(song).stream()
                .map(Pin::getGenre)
                .collect(Collectors.toList());

        Optional<GenreName> avgGenreName = songService.calculateAvgGenreName(genres);
        avgGenreName.ifPresent(song::setAvgGenreName);
        songRepository.save(song);
    }

    // 현재 로그인된 사용자가 핀의 생성자인지 확인
    @Transactional(readOnly = true)
    public Pin validatePinCreator(Long pinId) {
        Pin pin = getPinById(pinId);
        Member currentMember = memberService.getCurrentMember();
        if (!pin.getMember().getMemberId().equals(currentMember.getMemberId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        return pin;
    }

    // 특정 노래에 대한 핀 조회
    @Transactional(readOnly = true)
    public List<SongDetailsPinDto> getPinsForSong(Long songId, boolean onlyMyPins) {
        Song song = songService.getSongById(songId);
        List<Pin> pins;
        Member currentMember = memberService.getCurrentMember();
        Long currentMemberId = currentMember != null ? currentMember.getMemberId() : null;

        if (onlyMyPins && currentMember != null) {
            // 내 핀만 보기 - 현재 사용자의 모든 핀 가져오기(visibility 상관없음)
            pins = pinRepository.findAllBySongAndMember(song, currentMember);
        } else {
            // 전체 핀 보기
            // 1. "비공개 핀"에 대해 현재 사용자가 작성한 것만 가져오기
            pins = new ArrayList<>();
            if (currentMember != null) {
                pins.addAll(pinRepository.findAllBySongAndMemberAndVisibility(song, currentMember, Visibility.PRIVATE));
            }
            // 2. "공개 핀"에 대해 모든 사용자가 작성한 것 가져오기
            pins.addAll(pinRepository.findAllBySongAndVisibility(song, Visibility.PUBLIC));
        }

        return pins.stream()
                .map(pin -> {
                    return SongDetailsPinDto.from(pin, currentMemberId);
                })
                .collect(Collectors.toList());
    }

    // 내 핀 피드 조회
    @Transactional(readOnly = true)
    public FeedPinListResponseDto getMyFeedPins() {
        Member currentMember = memberService.getCurrentMember();
        List<Pin> pins = pinRepository.findAllByMember(currentMember);
        List<FeedPinUnitDto> feedPinList = pins.stream()
                .map(pin -> FeedPinUnitDto.from(pin, true))
                .collect(Collectors.toList());
        return new FeedPinListResponseDto(feedPinList, feedPinList.size());
    }

    // 타 유저의 공개 핀 피드 조회
    @Transactional(readOnly = true)
    public FeedPinListResponseDto getPublicFeedPins(Long memberId) {
        return getAllFeedPins(memberId, Visibility.PUBLIC);
    }

    // 피드 조회 공통 메서드
    @Transactional(readOnly = true)
    public FeedPinListResponseDto getAllFeedPins(Long memberId, Visibility visibility) {
        Member targetMember = memberService.getMemberById(memberId);
        List<Pin> pins = pinRepository.findAllByMemberAndVisibility(targetMember, visibility);
        List<FeedPinUnitDto> feedPinList = pins.stream()
                .map(pin -> FeedPinUnitDto.from(pin, false))
                .collect(Collectors.toList());
        return new FeedPinListResponseDto(feedPinList, feedPinList.size());
    }

    @Transactional(readOnly = true)
    public Pin getPinById(Long pinId) {
        return pinRepository.findById(pinId)
                .orElseThrow(() -> new CustomException(ErrorCode.PIN_NOT_FOUND));
    }

}
