package sws.songpin.domain.genre.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sws.songpin.domain.pin.entity.Pin;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", updatable = false)
    private Long genreId;

    @Column(name = "genre_name", length = 30)
    @NotNull
    @Enumerated(EnumType.STRING)
    private GenreName genreName;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Genre(Long genreId, GenreName genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.pins = new ArrayList<>();
    }
}
