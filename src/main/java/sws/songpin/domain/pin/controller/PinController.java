package sws.songpin.domain.pin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.pin.service.PinService;

@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @PostMapping
    public ResponseEntity<Void> createPin(@Valid @RequestBody PinRequestDto pinRequestDto) {
        pinService.createPin(pinRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
