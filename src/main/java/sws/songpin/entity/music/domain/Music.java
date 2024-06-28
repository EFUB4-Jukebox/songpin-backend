package sws.songpin.entity.music.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.global.BaseTimeEntity;

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

    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Music(Long musicId, String title, String artist, String imgPath) {
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.imgPath = imgPath;
        this.pins = new ArrayList<>();
    }
}
