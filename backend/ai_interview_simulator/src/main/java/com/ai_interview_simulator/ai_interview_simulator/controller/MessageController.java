//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.controller;

import com.ai_interview_simulator.ai_interview_simulator.dto.request.MessageRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.ApiResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.MessageResponse;
import com.ai_interview_simulator.ai_interview_simulator.service.InterviewAiService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/messages"})
public class MessageController {
    private final InterviewAiService interviewAiService;

    public MessageController(InterviewAiService interviewAiService) {
        this.interviewAiService = interviewAiService;
    }

    @PostMapping({"/start/{interviewId}"})
    public ResponseEntity<ApiResponse<MessageResponse>> startInterview(@PathVariable Long interviewId) {
        MessageResponse response = this.interviewAiService.generateOpeningMessage(interviewId);
        return ResponseEntity.ok(ApiResponse.success("Interview started", response));
    }

    @PostMapping({"/send"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> sendMessage(@RequestBody @Valid MessageRequest request) {
        Map<String, Object> response = this.interviewAiService.processUserMessage(request.getInterviewId(), request.getContent());
        return ResponseEntity.ok(ApiResponse.success("Message sent", response));
    }

    @PostMapping({"/finish/{interviewId}"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> finishInterview(@PathVariable Long interviewId) {
        Map<String, Object> response = this.interviewAiService.finishInterview(interviewId);
        return ResponseEntity.ok(ApiResponse.success("Interview completed successfully", response));
    }

    @GetMapping({"/status/{interviewId}"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInterviewStatus(@PathVariable Long interviewId) {
        Map<String, Object> status = this.interviewAiService.getInterviewStatus(interviewId);
        return ResponseEntity.ok(ApiResponse.success("Status retrieved", status));
    }
}
