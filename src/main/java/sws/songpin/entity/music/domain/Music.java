package sws.songpin.entity.music.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.genre.domain.Genre;
import sws.songpin.entity.musicGenre.domain.MusicGenre;
import sws.songpin.entity.pin.domain.Pin;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id", updatable = false)
    private Long musicId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "img_path")
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avg_genre")
    private Genre avgGenre;

    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenre> musicGenres = new ArrayList<>();

    @Builder
    public Music(Long musicId, String title, String artist, String imgPath, Genre avgGenre) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.imgPath = imgPath;
        this.avgGenre = avgGenre;
        this.pins = new ArrayList<>();
        this.musicGenres = new ArrayList<>();
    }
}
