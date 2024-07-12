package sws.songpin.domain.song.spotify;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpotifyUtilTest {
    @Autowired
    private SpotifyUtil spotifyUtil;

    @Test
    void authenticate() {
        String accessToken = spotifyUtil.authenticate();
        assertNotNull(accessToken);
        assertNotEquals("error", accessToken);
    }

    @Test
    void searchTracks() {
        String query = "test";
        int offset = 0;
        List<Track> tracks = spotifyUtil.searchTracks(query, offset);
        assertNotNull(tracks);
        assertTrue(tracks.size() > 0);
    }
}