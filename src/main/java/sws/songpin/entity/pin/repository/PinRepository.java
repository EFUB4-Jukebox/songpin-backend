package sws.songpin.entity.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.entity.pin.domain.Pin;

public interface PinRepository extends JpaRepository <Pin, Long> {
}
