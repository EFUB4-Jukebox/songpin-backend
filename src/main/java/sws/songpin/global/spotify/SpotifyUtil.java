package sws.songpin.global.spotify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpotifyUtil {

    private final SpotifyApi spotifyApi;

    // limit, offset은 검색결과의 페이징 또는 무한스크롤을 위함
    private static final int LIMIT = 8;
    private static final int OFFSET = 0;

    // 동기적
    public String authenticate() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return spotifyApi.getAccessToken();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Spotify Error: " + e.getMessage());
            return "error";
        }
    }

    public List<Track> searchTracks(String query) {
        authenticate();

        String searchQuery = "track:" + query + " artist:" + query; // 가수 또는 제목(트랙)에 대해 검색
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).limit(LIMIT).offset(OFFSET).build();
        List<Track> tracks = new ArrayList<>();
        try {
            Paging<Track> trackPaging = searchTracksRequest.execute();
            Track[] items = trackPaging.getItems();
            for (Track track : items) {
                tracks.add(track);
            }
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Spotify Error: " + e.getMessage());
        }

        return tracks;
    }

}