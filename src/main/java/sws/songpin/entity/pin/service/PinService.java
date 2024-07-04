package sws.songpin.entity.pin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.entity.pin.repository.PinRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;

    // 임시 개발용
    public Optional<Pin> getPin(Long id) {
        return pinRepository.findById(id);
    }

}
