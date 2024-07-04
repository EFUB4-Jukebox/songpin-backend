package sws.songpin.entity.song.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.genre.domain.GenreName;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.entity.externalApi.model.ProviderType;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id", updatable = false)
    private Long songId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", length = 100, nullable = false)
    private String artist;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "provider_type", length = 20)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "provider_track_code")
    private String providerTrackCode;

    @Column(name = "avg_genre", length = 30)
    @Enumerated(EnumType.STRING)
    private GenreName avgGenre;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Song(Long songId, String title, String artist, String imgPath,
                ProviderType providerType, String providerTrackCode, GenreName avgGenre) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.imgPath = imgPath;
        this.providerType = providerType;
        this.providerTrackCode = providerTrackCode;
        this.avgGenre = avgGenre;
        this.pins = new ArrayList<>();
    }
}
