package com.lion.server.domain.analysis.service;

import com.lion.server.infra.ai.DeepLClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class DeepLTranslationService {

    private final DeepLClient deepLClient;

    /**
     * 입력받은 텍스트를 대상 언어(targetLang)로 번역
     *
     * @param text       번역할 원문 텍스트
     * @param targetLang 목표 언어 코드 (예: "EN-US", "JA", "ZH")
     * @return DeepL 번역 응답 객체
     */
    public DeepLClient.TranslationResponse translate(String text, String targetLang) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("번역할 텍스트가 비어 있습니다.");
        }

        return deepLClient.translate(text, targetLang);
    }
}