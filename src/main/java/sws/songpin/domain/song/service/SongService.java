package sws.songpin.domain.song.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.dto.response.PlaceSearchResponseDto;
import sws.songpin.domain.place.dto.response.PlaceUnitDto;
import sws.songpin.domain.song.dto.request.SongAddRequestDto;
import sws.songpin.domain.song.dto.response.*;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.spotify.SpotifyUtil;
import se.michaelthelin.spotify.model_objects.specification.Track;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SongService {

    private final SpotifyUtil spotifyUtil;
    private final SongRepository songRepository;
    private final PinRepository pinRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<SpotifySearchDto> searchTracks(String keyword, int offset) {
        List<Track> tracks = spotifyUtil.searchTracks(keyword, offset);
        return tracks.stream()
                .map(track -> new SpotifySearchDto(
                        track.getName(),
                        spotifyUtil.getArtistNames(track),
                        track.getAlbum().getImages().length > 0 ? track.getAlbum().getImages()[0].getUrl() : null,
                        track.getId()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<GenreName> calculateAvgGenreName(List<Genre> genres) {
        Map<GenreName, Long> genreCount = genres.stream()
                .collect(Collectors.groupingBy(Genre::getGenreName, Collectors.counting()));

        return genreCount.entrySet().stream()
                .sorted(Map.Entry.<GenreName, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    // 노래 검색
    @Transactional(readOnly = true)
    public SongSearchResponseDto searchSongs(String keyword, SortBy sortBy, Pageable pageable) {
        String keywordNoSpaces = keyword.replace(" ", "");
        Page<Object[]> songPage;

        switch (sortBy) {
            case NEWEST -> songPage = songRepository.findAllBySongNameOrArtistContainingIgnoreSpacesOrderByNewest(keywordNoSpaces, pageable);
            case COUNT -> songPage = songRepository.findAllBySongNameOrArtistContainingIgnoreSpacesOrderByCount(keywordNoSpaces, pageable);
            case ACCURACY -> songPage = songRepository.findAllBySongNameOrArtistContainingIgnoreSpacesOrderByAccuracy(keywordNoSpaces, pageable);
            default -> throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }

        Page<SongUnitDto> songUnitPage = songPage.map(this::convertToSongUnitDto);

        return SongSearchResponseDto.from(songUnitPage);
    }

    public SongUnitDto convertToSongUnitDto(Object[] objects) {
        return new SongUnitDto(
                new SongInfoDto(
                        ((Number) objects[0]).longValue(),
                        (String) objects[1],
                        (String) objects[2],
                        (String) objects[3]
                ),
                GenreName.valueOf((String) objects[4]),
                ((Number) objects[5]).intValue()
        );
    }

    public Song createSong(SongAddRequestDto songRequestDto) {
        Song song = songRequestDto.toEntity();
        return songRepository.save(song);
    }

    @Transactional(readOnly = true)
    public Optional<Song> getSongByProviderTrackCode(String providerTrackCode) {
        return songRepository.findByProviderTrackCode(providerTrackCode);
    }

    public Song getOrCreateSong(SongAddRequestDto songRequestDto) {
        return getSongByProviderTrackCode(songRequestDto.providerTrackCode())
                .orElseGet(() -> createSong(songRequestDto));
    }

    // 특정 노래에 대한 상세정보
    @Transactional(readOnly = true)
    public SongDetailsResponseDto getSongDetails(Long songId) {
        Song song = getSongById(songId);
        Member currentMember = memberService.getCurrentMember();
        LocalDate lastListenedDate = getLastListenedDate(song, currentMember);
        int pinCount = pinRepository.countBySong(song);
        return SongDetailsResponseDto.from(song, lastListenedDate, pinCount);
    }

    @Transactional(readOnly = true)
    public LocalDate getLastListenedDate(Song song, Member member) {
        return pinRepository.findTopBySongAndCreatorOrderByListenedDateDesc(song, member)
                .map(Pin::getListenedDate)
                .orElse(null); // null 반환하면 "아직 듣지 않음"으로 프론트에서 표시
    }

    @Transactional(readOnly = true)
    public Song getSongById(Long songId) {
        return songRepository.findById(songId)
                .orElseThrow(() -> new CustomException(ErrorCode.SONG_NOT_FOUND));
    }
}
