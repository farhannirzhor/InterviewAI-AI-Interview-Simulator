//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.controller;

import com.ai_interview_simulator.ai_interview_simulator.dto.response.ApiResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.EvaluationResponse;
import com.ai_interview_simulator.ai_interview_simulator.service.EvaluationService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/evaluation"})
public class EvaluationController {
    private static final Logger log = LoggerFactory.getLogger(EvaluationController.class);
    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping({"/{interviewId}/generate"})
    public ResponseEntity<ApiResponse<EvaluationResponse>> generateEvaluation(@PathVariable Long interviewId) {
        log.info("Evaluation requested for interview id={}", interviewId);
        EvaluationResponse response = this.evaluationService.generateEvaluation(interviewId);
        return ResponseEntity.ok(ApiResponse.success("Evaluation generated successfully", response));
    }

    @GetMapping({"/{interviewId}"})
    public ResponseEntity<ApiResponse<EvaluationResponse>> getSavedEvaluation(@PathVariable Long interviewId) {
        EvaluationResponse response = this.evaluationService.getSavedEvaluation(interviewId);
        return ResponseEntity.ok(ApiResponse.success("Evaluation retrieved", response));
    }

    @GetMapping({"/{interviewId}/summary"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> getScoreSummary(@PathVariable Long interviewId) {
        Map<String, Object> summary = this.evaluationService.getScoreSummary(interviewId);
        return ResponseEntity.ok(ApiResponse.success("Score summary retrieved", summary));
    }
}
