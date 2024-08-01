package sws.songpin.domain.song.spotify;

import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpotifyUtil {

    private final SpotifyApi spotifyApi;

    // 페이징 설정
    private static final int LIMIT = 20;

    // 동기식
    public String authenticate() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return spotifyApi.getAccessToken();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    public List<Track> searchTracks(String query, int offset) {
        authenticate();

        List<Track> allTracks = new ArrayList<>();
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query)
                .limit(LIMIT)
                .offset(offset)
                .market(CountryCode.KR)
                .build();

        try {
            Paging<Track> trackPaging = searchTracksRequest.execute();
            Track[] items = trackPaging.getItems();

            for (Track track : items) {
                allTracks.add(track);
            }

        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
        }

        // 쿼리에서 공백을 제거하고 소문자로 변환
        String normalizedQuery = query.replaceAll("\\s+", "").toLowerCase();
        String[] queryWords = query.toLowerCase().split("\\s+");
        List<Track> matchedTracks = new ArrayList<>();

        for (Track track : allTracks) {
            // 트랙 이름과 아티스트 이름을 결합한 문자열 생성
            String trackName = track.getName().replaceAll("\\s+", "").toLowerCase();
            String artistNames = getArtistNames(track).replaceAll("\\s+", "").toLowerCase();
            String combinedString = trackName + " " + artistNames;

            // 쿼리의 모든 단어가 결합된 문자열에 포함되는지 확인
            boolean allWordsMatch = true;
            for (String word : queryWords) {
                if (!combinedString.contains(word)) {
                    allWordsMatch = false;
                    break;
                }
            }

            if (allWordsMatch) {
                matchedTracks.add(track);
            }
        }

        return matchedTracks;
    }

    public String getArtistNames(Track track) {
        List<String> artistNames = new ArrayList<>();
        for (var artist : track.getArtists()) {
            artistNames.add(artist.getName());
        }
        return String.join(" ", artistNames);
    }
}
