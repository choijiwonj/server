package com.lion.server.infra.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * DeepL API 번역 통신을 담당하는 Client 컴포넌트
 */
@Slf4j
@Component
public class DeepLClient {

    @Value("${deepl.api.key}")
    private String apiKey;

    @Value("${deepl.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 원문 텍스트를 대상 언어로 번역 (원문 언어는 DeepL 자동 감지)
     *
     * @param text       번역할 원문
     * @param targetLang 목표 언어 코드 (예: "EN-US", "JA", "DE")
     * @return TranslationResponse 번역 결과 및 감지된 언어 정보
     */
    public TranslationResponse translate(String text, String targetLang) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "DeepL-Auth-Key " + apiKey);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("text", text);
        body.add("target_lang", targetLang.toUpperCase());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

            if (response.getBody() != null) {
                return parseResponse(response.getBody(), targetLang);
            }
        } catch (Exception e) {
            log.error("DeepL API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new IllegalStateException("DeepL 번역 API 호출에 실패했습니다.", e);
        }

        throw new IllegalStateException("DeepL 번역 응답이 비어 있습니다.");
    }

    /**
     * DeepL 응답 객체 파싱
     */
    @SuppressWarnings("unchecked")
    private TranslationResponse parseResponse(Map responseBody, String targetLang) {
        List<Map<String, Object>> translations = (List<Map<String, Object>>) responseBody.get("translations");
        if (translations != null && !translations.isEmpty()) {
            Map<String, Object> result = translations.get(0);
            String translatedText = (String) result.get("text");
            String detectedSourceLang = (String) result.get("detected_source_language");

            return new TranslationResponse(translatedText, detectedSourceLang, targetLang);
        }
        throw new IllegalStateException("DeepL 응답 데이터 파싱 실패");
    }

    /**
     * DeepL 번역 응답 DTO
     */
    public record TranslationResponse(
            String translatedText,
            String detectedSourceLang,
            String targetLang
    ) {}
}