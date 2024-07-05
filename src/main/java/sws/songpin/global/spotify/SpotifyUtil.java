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

    // limit, offset은 페이징 또는 무한스크롤을 위함
    public List<Track> searchTracks(String query, int limit, int offset) {
        authenticate();

        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).limit(limit).offset(offset).build();
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