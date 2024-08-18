package sws.songpin.domain.pin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.genre.service.GenreService;
import sws.songpin.domain.member.dto.response.MyPinSearchResponseDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.dto.request.PinAddRequestDto;
import sws.songpin.domain.pin.dto.request.PinUpdateRequestDto;
import sws.songpin.domain.pin.dto.response.*;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;
import sws.songpin.domain.place.service.PlaceService;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.playlistpin.repository.PlaylistPinRepository;
import sws.songpin.domain.song.dto.response.*;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.service.SongService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sws.songpin.global.common.EscapeSpecialCharactersService.escapeSpecialCharacters;

@Service
@Transactional
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final SongRepository songRepository;
    private final PlaceRepository placeRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final MemberService memberService;
    private final SongService songService;
    private final PlaceService placeService;
    private final GenreService genreService;

    // 음악 핀 생성 - 노래, 장소가 없다면 추가하기
    public Long createPin(PinAddRequestDto pinAddRequestDto) {
        Member currentMember = memberService.getCurrentMember();
        Song finalSong = songService.getOrCreateSong(pinAddRequestDto.song());
        Place finalPlace = placeService.getOrCreatePlace(pinAddRequestDto.place());
        Genre genre = genreService.getGenreByGenreName(pinAddRequestDto.genreName());

        Pin pin = Pin.builder()
                .listenedDate(pinAddRequestDto.listenedDate())
                .memo(pinAddRequestDto.memo())
                .visibility(pinAddRequestDto.visibility())
                .creator(currentMember)
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
        updateSongAvgGenreName(pin.getSong());
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
        Place place = pin.getPlace();
        pinRepository.delete(pin);
        updateSongAvgGenreName(pin.getSong());
        if (place != null && pinRepository.countByPlace(place) == 0) {
            placeRepository.delete(place);
        }
    }

    public void updateSongAvgGenreName(Song song) {
        List<Genre> genres = pinRepository.findBySong(song).stream()
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
        if (!pin.getCreator().getMemberId().equals(currentMember.getMemberId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        return pin;
    }

    // 특정 노래에 대한 핀 조회
    @Transactional(readOnly = true)
    public SongDetailsPinListResponseDto getPinsForSong(Long songId, boolean onlyMyPins, Pageable pageable) {
        Song song = songService.getSongById(songId);
        Member currentMember = memberService.getCurrentMember();
        Long currentMemberId = currentMember != null ? currentMember.getMemberId() : null;
        Page<Pin> pinPage;

        if (onlyMyPins) {
            // 내 핀만 보기 - 현재 사용자의 모든 핀 가져오기
            pinPage = pinRepository.findAllBySongAndCreator(song, currentMember, pageable);
        } else {
            // 전체 핀 보기 - 내 핀 가져오기
            pinPage = pinRepository.findAllBySong(song, pageable);
        }
        Page<SongDetailsPinDto> songDetailsPinPage = pinPage.map(pin -> {
            Boolean isMine = pin.getCreator().getMemberId().equals(currentMemberId);
            String memo = getMemoContent(pin.getMemo(), pin.getVisibility(), isMine);
            return SongDetailsPinDto.from(pin, memo, isMine);
        });
        return SongDetailsPinListResponseDto.from(songDetailsPinPage);
    }

    // 타 유저 핀피드 조회
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getMemberPinFeed(String handle, Pageable pageable) {
        Member member = memberService.getActiveMemberByHandle(handle);
        Page<Object[]> pinFeedPage = pinRepository.findPinFeed(member, pageable);
        return getPinFeedResponse(pinFeedPage, false);
    }

    // 내 핀피드(마이페이지 핀피드) 조회
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getMyPinFeed(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Object[]> pinFeedPage = pinRepository.findPinFeed(currentMember, pageable);
        return getPinFeedResponse(pinFeedPage, true);
    }

    // 핀피드 조회 공통 메서드
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getPinFeedResponse(Page<Object[]> pinFeedPage, boolean isMine) {
        Page<PinFeedUnitDto> pinFeedUnitPage = pinFeedPage.map(objects -> {
            Long pinId = ((Number) objects[0]).longValue();
            SongInfoDto songInfo = new SongInfoDto(
                    ((Number) objects[1]).longValue(),
                    (String) objects[2],
                    (String) objects[3],
                    (String) objects[4]
            );
            LocalDate listenedDate = (LocalDate) objects[5];
            String placeName = (String) objects[6];
            double latitude = ((Number) objects[7]).doubleValue();
            double longitude = ((Number) objects[8]).doubleValue();
            GenreName genreName = GenreName.valueOf(objects[9].toString());
            String memo = (String) objects[10];
            Visibility visibility = Visibility.valueOf(objects[11].toString());
            String adjustedMemo = getMemoContent(memo, visibility, isMine);
            return new PinFeedUnitDto(
                    pinId,
                    songInfo,
                    listenedDate,
                    placeName,
                    latitude,
                    longitude,
                    genreName,
                    adjustedMemo,
                    visibility,
                    isMine
            );
        });
        return PinFeedListResponseDto.from(pinFeedUnitPage);
    }

    // 내 메모 또는 공개메모핀이 아니면 "비공개인 메모입니다." 반환
    @Transactional(readOnly = true)
    public String getMemoContent(String memo, Visibility visibility, boolean isMine) {
        return (isMine || visibility == Visibility.PUBLIC) ? memo : "비공개인 메모입니다.";
    }

    // 마이페이지 캘린더
    @Transactional(readOnly = true)
    public PinBasicListResponseDto getMyPinFeedForMonth(int year, int month) {
        Member currentMember = memberService.getCurrentMember();
        List<Pin> pins = pinRepository.findAllByCreatorAndDate(currentMember,year, month);
        List<PinBasicUnitDto> pinList = pins.stream()
                .map(pin -> PinBasicUnitDto.from(pin, true))
                .collect(Collectors.toList());
        return new PinBasicListResponseDto(pinList);
    }

    // 마이페이지에서 내 핀 검색
    @Transactional(readOnly = true)
    public MyPinSearchResponseDto searchMyPins(String keyword, Pageable pageable) {
        // 키워드의 이스케이프 처리 및 띄어쓰기 제거
        String escapedWord = escapeSpecialCharacters(keyword);
        String keywordNoSpaces = escapedWord.replace(" ", "");

        Long currentMemberId = memberService.getCurrentMember().getMemberId();
        Page<Object[]> myPinPage = pinRepository.findAllBySongNameOrArtistOrPlaceNameContainingIgnoreSpaces(currentMemberId, keywordNoSpaces, pageable);

        Page<PinBasicUnitDto> myPinUnitPage = myPinPage.map(objects -> {
            Long pinId = ((Number) objects[0]).longValue();
            SongInfoDto songInfo = new SongInfoDto(
                    ((Number) objects[1]).longValue(),
                    (String) objects[2],
                    (String) objects[3],
                    (String) objects[4]
            );
            // java.sql.Date를 LocalDate로 변환
            java.sql.Date sqlDate = (java.sql.Date) objects[5];
            LocalDate listenedDate = sqlDate.toLocalDate();
            String placeName = (String) objects[6];
            double latitude = ((Number) objects[7]).doubleValue();
            double longitude = ((Number) objects[8]).doubleValue();
            GenreName genreName = GenreName.valueOf(objects[9].toString());
            Long creatorId = ((Number) objects[10]).longValue();
            Boolean isMine = creatorId.equals(currentMemberId);

            return new PinBasicUnitDto(pinId, songInfo, listenedDate, placeName, latitude, longitude, genreName, isMine);
        });

        return MyPinSearchResponseDto.from(myPinUnitPage);
    }

    @Transactional(readOnly = true)
    public Pin getPinById(Long pinId) {
        return pinRepository.findById(pinId)
                .orElseThrow(() -> new CustomException(ErrorCode.PIN_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public PinExistingInfoResponseDto getPinInfo(Long pinId) {
        Pin pin = getPinById(pinId);
        return PinExistingInfoResponseDto.from(pin);
    }

}
