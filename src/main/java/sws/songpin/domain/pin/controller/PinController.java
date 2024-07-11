package sws.songpin.domain.pin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.song.dto.response.SongDetailResponseDto;

import java.net.URI;

@Tag(name = "Pin", description = "Pin 관련 API입니다.")
@RestController
@RequestMapping("/pins")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;

    @PostMapping
    @Operation(summary = "핀 생성", description = "핀을 생성합니다.")
    public ResponseEntity<Void> createPin(@Valid @RequestBody PinRequestDto pinRequestDto) {
        SongDetailResponseDto songDetailResponseDto = pinService.createPin(pinRequestDto);
        URI location = URI.create("/songs/" + songDetailResponseDto.songId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
