package com.lion.server.domain.analysis.dto;

import java.util.List;


public record AnalysisResponseDto(
        String originalText,
        String translatedText,   // DeepL 번역 결과
        String overallRiskLevel, // HIGH, MEDIUM, LOW
        List<RiskDetail> details
) {
    // Null Safety 및 불변성을 보장하는 Compact Constructor
    public AnalysisResponseDto {
        details = (details == null) ? List.of() : List.copyOf(details);
    }

    public record RiskDetail(
            int startIndex,
            int endIndex,
            String originalSnippet,
            String riskLevel,
            String reason,
            String suggestion
    ) {}
}