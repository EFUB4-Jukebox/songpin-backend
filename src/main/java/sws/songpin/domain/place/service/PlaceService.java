package sws.songpin.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.repository.PlaceRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final RestTemplate restTemplate;

    @Value("${kakao.api-key}")
    private String kakaoApiKey;

    public Optional<Place> getPlace(String placeName) {
        return placeRepository.findByPlaceName(placeName);
    }

    private Map<String, Object> searchPlace(String query) {
        try {
            // 1. URL 인코딩
            String queryEncoded = URLEncoder.encode(query, "UTF-8");
            String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + queryEncoded;

            // 2. 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 3. API 요청 보내기
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
            List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");

            if (documents.isEmpty()) {
                throw new RuntimeException("No place found for the query: " + query);
            }

            // 4. 응답 데이터 파싱
            return documents.get(0);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding error: " + e.getMessage());
        }
    }

    public String searchPlaceAsJson(String query) {
            return searchPlace(query).toString();
    }
}
