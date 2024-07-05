package sws.songpin.domain.song.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Track;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.global.spotify.SpotifyUtil;
import sws.songpin.domain.genre.entity.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SpotifyUtil spotifyUtil;

    public List<Song> createSongsFromSpotify(String query, int limit, int offset) {
        List<Track> tracks = spotifyUtil.searchTracks(query, limit, offset);
        List<Song> songs = new ArrayList<>();

        for (Track track : tracks) {
            String imgPath = track.getAlbum().getImages().length > 0 ? track.getAlbum().getImages()[0].getUrl() : null;
            String providerTrackCode = track.getId();

            Song song = Song.builder()
                    .title(track.getName())
                    .artist(track.getArtists()[0].getName())
                    .imgPath(imgPath)
                    .providerTrackCode(providerTrackCode)
                    .build();

            songs.add(song);
        }

        return songs;
    }

    public GenreName findMostCommonGenre(List<Genre> genres) {
        Map<GenreName, Long> genreCount = genres.stream()
                .collect(Collectors.groupingBy(Genre::getGenreName, Collectors.counting()));

        return genreCount.entrySet().stream()
                .sorted(Map.Entry.<GenreName, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .findFirst()
                .get(); // 예외를 던지지 않고 결과가 항상 있다고 가정
    }
}
