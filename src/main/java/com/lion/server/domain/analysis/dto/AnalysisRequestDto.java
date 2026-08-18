package com.lion.server.domain.analysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record AnalysisRequestDto(
        @NotBlank(message = "검토할 문장을 입력해주세요.")
        @Size(max = 500, message = "문장은 최대 500자까지 입력 가능합니다.")
        String text,

        @NotBlank(message = "목표 번역 언어를 선택해주세요.")
        String targetLang, // 예: "EN-US", "JA", "ZH"

        @NotBlank(message = "문화권 코드를 입력해주세요.")
        String cultureCode // 예: "US", "JP", "CN"
) {}