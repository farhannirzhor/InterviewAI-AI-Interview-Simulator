//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.controller;

import com.ai_interview_simulator.ai_interview_simulator.dto.request.InterviewRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.ApiResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.InterviewResponse;
import com.ai_interview_simulator.ai_interview_simulator.service.InterviewAiService;
import com.ai_interview_simulator.ai_interview_simulator.service.InterviewService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/interviews"})
public class InterviewController {
    private final InterviewService interviewService;
    private final InterviewAiService interviewAiService;

    public InterviewController(InterviewService interviewService, InterviewAiService interviewAiService) {
        this.interviewService = interviewService;
        this.interviewAiService = interviewAiService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InterviewResponse>> createInterview(@RequestBody @Valid InterviewRequest request) {
        InterviewResponse response = this.interviewService.createInterview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Interview created successfully", response));
    }

    @GetMapping({"/my"})
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getMyInterviews() {
        List<InterviewResponse> response = this.interviewService.getMyInterviews();
        return ResponseEntity.ok(ApiResponse.success("Interviews retrieved", response));
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<InterviewResponse>> getInterviewById(@PathVariable Long id) {
        InterviewResponse response = this.interviewService.getInterviewById(id);
        return ResponseEntity.ok(ApiResponse.success("Interview retrieved", response));
    }

    @GetMapping({"/{id}/message-count"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMessageCount(@PathVariable Long id) {
        long count = this.interviewService.getMessageCount(id);
        boolean limitReached = this.interviewService.isLimitReached(id);
        Map<String, Object> data = Map.of("interviewId", id, "messageCount", count, "messageLimit", 20, "limitReached", limitReached, "remaining", Math.max(0L, 20L - count));
        return ResponseEntity.ok(ApiResponse.success("Message count retrieved", data));
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<ApiResponse<Void>> deleteInterview(@PathVariable Long id) {
        this.interviewService.deleteInterview(id);
        return ResponseEntity.ok(ApiResponse.success("Interview deleted successfully"));
    }

    @PostMapping({"/{id}/finish"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> finishInterview(@PathVariable Long id) {
        Map<String, Object> response = this.interviewAiService.finishInterview(id);
        return ResponseEntity.ok(ApiResponse.success("Interview finished", response));
    }
}
