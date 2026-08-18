package com.lion.server.domain.analysis.service;


import com.lion.server.infra.ai.GeminiClient;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

@Service
public class GeminiAnalysisService {

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiAnalysisService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public Map<String, Object> analyze(String originalText, String translatedText, String cultureCode) {
        try {
            String rawJsonResponse = geminiClient.analyzeRiskAndRefine(originalText, translatedText, cultureCode);

            // Markdown tag 제거나 백틱 제거 처리
            String cleanJson = rawJsonResponse.replaceAll("```json", "").replaceAll("```", "").trim();

            return objectMapper.readValue(cleanJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Gemini 응답 파싱 실패: " + e.getMessage());
        }
    }
}