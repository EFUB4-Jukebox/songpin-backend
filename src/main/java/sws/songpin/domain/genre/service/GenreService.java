package sws.songpin.domain.genre.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.genre.repository.GenreRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    // DB에 이미 넣어놨다면 필요없음
    @PostConstruct
    public void initGenres() {
        for (GenreName genreName : GenreName.values()) {
            genreRepository.findByGenreName(genreName).orElseGet(() -> {
                Genre genre = Genre.builder()
                        .genreName(genreName)
                        .build();
                return genreRepository.save(genre);
            });
        }
    }

    public Genre getGenreByGenreName(GenreName genreName) {
        return genreRepository.findByGenreName(genreName)
                .orElseThrow(() -> new CustomException(ErrorCode.GENRE_NOT_FOUND));
    }

}
