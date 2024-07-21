package sws.songpin.domain.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.statistics.dto.response.StatsGenreResponseDto;
import sws.songpin.domain.statistics.dto.response.StatsOverallResponseDto;
import sws.songpin.domain.statistics.service.StatisticsService;

@Tag(name = "Statistics", description = "서비스 통계 데이터를 보여줍니다.")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "서비스 통계-종합", description = "종합적인 4가지 통계 데이터를 제공합니다.")
    @GetMapping("/overall")
    public ResponseEntity<?> overallStats() {
        StatsOverallResponseDto responseDto = statisticsService.getOverallStats();
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "서비스 통계-장르별 장소, 노래", description = "장르별로 가장 많은 핀이 등록된 장소, 노래 정보를 전달합니다.")
    @GetMapping("/genre")
    public ResponseEntity<?> placeAndSongStatsByGenre() {
        StatsGenreResponseDto responseDto = statisticsService.getPlaceAndSongStatsByGenre();
        return ResponseEntity.ok(responseDto);
    }
}
