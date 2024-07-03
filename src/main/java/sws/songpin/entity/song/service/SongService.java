package sws.songpin.entity.song.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sws.songpin.entity.song.domain.Song;
import sws.songpin.entity.song.repository.SongRepository;

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