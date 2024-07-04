package sws.songpin.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.pin.domain.Pin;

public interface PinRepository extends JpaRepository <Pin, Long> {
}
