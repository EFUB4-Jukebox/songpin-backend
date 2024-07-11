package sws.songpin.domain.pin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.pin.service.PinService;

@Tag(name = "Pin", description = "Pin 관련 API입니다.")
@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    // responseDto로 반환하도록 수정해야할듯
    @PostMapping
    @Operation(summary = "핀 생성", description = "핀을 생성합니다.")
    public ResponseEntity<Void> createPin(@Valid @RequestBody PinRequestDto pinRequestDto) {
        pinService.createPin(pinRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
