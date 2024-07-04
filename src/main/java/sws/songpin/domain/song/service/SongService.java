package sws.songpin.domain.song.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;

    // 임시 개발용
    public Optional<Song> getSong(Long id) {
        return songRepository.findById(id);
    }

}