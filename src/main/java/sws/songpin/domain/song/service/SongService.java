package sws.songpin.domain.song.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.spotify.SpotifyUtil;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SpotifyUtil spotifyUtil;
    private final SongRepository songRepository;

    public List<PinRequestDto.SongRequestDto> searchTracks(String keyword, int offset) {
        List<Track> tracks = spotifyUtil.searchTracks(keyword, offset);
        return tracks.stream()
                .map(track -> new PinRequestDto.SongRequestDto(
                        track.getName(),
                        spotifyUtil.getArtistNames(track),
                        track.getAlbum().getImages().length > 0 ? track.getAlbum().getImages()[0].getUrl() : null,
                        track.getId()
                ))
                .collect(Collectors.toList());
    }

    public Optional<Song> getSongByProviderTrackCode(String providerTrackCode) {
        return songRepository.findByProviderTrackCode(providerTrackCode);
    }

    public Optional<GenreName> calculateAvgGenreName(List<Genre> genres) {
        Map<GenreName, Long> genreCount = genres.stream()
                .collect(Collectors.groupingBy(Genre::getGenreName, Collectors.counting()));

        return genreCount.entrySet().stream()
                .sorted(Map.Entry.<GenreName, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    // 임시 개발용
    public Optional<Song> getSong(Long id) {
        return songRepository.findById(id);
    }
}
