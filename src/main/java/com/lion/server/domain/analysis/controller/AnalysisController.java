package com.lion.server.domain.analysis.controller;

import com.lion.server.domain.analysis.dto.AnalysisRequestDto;
import com.lion.server.domain.analysis.dto.AnalysisResponseDto;
import com.lion.server.domain.analysis.service.AiPipelineService;
import com.lion.server.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 필요 시 특정 오리진(예: "http://localhost:5173")으로 제한 가능
public class AnalysisController {

    private final AiPipelineService aiPipelineService;

    @PostMapping("/run")
    public ResponseEntity<ApiResponse<AnalysisResponseDto>> runAnalysis(
            @Valid @RequestBody AnalysisRequestDto request
    ) {
        AnalysisResponseDto result = aiPipelineService.runPipeline(request);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}