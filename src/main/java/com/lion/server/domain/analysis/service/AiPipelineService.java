package com.lion.server.domain.analysis.service;

import com.lion.server.domain.analysis.dto.AnalysisRequestDto;
import com.lion.server.domain.analysis.dto.AnalysisResponseDto;
import com.lion.server.infra.ai.DeepLClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiPipelineService {

    private final DeepLTranslationService deepLTranslationService;
    private final GeminiAnalysisService geminiAnalysisService;

    /**
     * RealMode 분석 파이프라인
     * 1. DeepL 1차 번역
     * 2. Gemini 문화적 뉘앙스/위험도 분석
     * 3. 최종 통합 응답 조립
     */
    public AnalysisResponseDto runPipeline(AnalysisRequestDto request) {
        // Step 1: DeepL 1차 번역 수행
        DeepLClient.TranslationResponse translationResponse = deepLTranslationService.translate(
                request.text(),
                request.targetLang()
        );
        String translatedText = translationResponse.translatedText();

        // Step 2: Gemini 문화적 위험 요소 분석
        Map<String, Object> geminiResult = geminiAnalysisService.analyze(
                request.text(),
                translatedText,
                request.cultureCode()
        );

        String overallRiskLevel = (String) geminiResult.getOrDefault("overallRiskLevel", "LOW");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> detailsRaw = (List<Map<String, Object>>) geminiResult.get("details");

        List<AnalysisResponseDto.RiskDetail> details = (detailsRaw == null) ? Collections.emptyList() :
                detailsRaw.stream().map(d -> new AnalysisResponseDto.RiskDetail(
                        d.get("startIndex") instanceof Number n ? n.intValue() : 0,
                        d.get("endIndex") instanceof Number n ? n.intValue() : 0,
                        (String) d.getOrDefault("originalSnippet", ""),
                        (String) d.getOrDefault("riskLevel", "LOW"),
                        (String) d.getOrDefault("reason", ""),
                        (String) d.getOrDefault("suggestion", "")
                )).toList();

        // Step 3: 최종 응답 조립 (sessionId 제거됨)
        return new AnalysisResponseDto(
                request.text(),
                translatedText,
                overallRiskLevel,
                details
        );
    }
}