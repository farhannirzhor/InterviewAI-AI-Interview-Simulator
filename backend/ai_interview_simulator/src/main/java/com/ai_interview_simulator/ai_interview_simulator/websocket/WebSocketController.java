//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.websocket;

import com.ai_interview_simulator.ai_interview_simulator.dto.request.WebSocketMessageRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.WebSocketMessageResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.WebSocketMessageResponse.MessageType;
import com.ai_interview_simulator.ai_interview_simulator.exception.MessageLimitExceededException;
import com.ai_interview_simulator.ai_interview_simulator.service.InterviewAiService;
import com.ai_interview_simulator.ai_interview_simulator.service.UserService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final InterviewAiService interviewAiService;
    private final UserService userService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, InterviewAiService interviewAiService, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.interviewAiService = interviewAiService;
        this.userService = userService;
    }

    @MessageMapping({"/interview/{interviewId}/send"})
    public void sendMessage(@DestinationVariable Long interviewId, @Payload WebSocketMessageRequest request, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        log.info("WS message received — interviewId={} from user={}", interviewId, principal != null ? principal.getName() : "unknown");

        try {
            Map<String, Object> result = this.interviewAiService.processUserMessage(interviewId, request.getContent());
            Object userMsgObj = result.get("userMessage");
            Object aiMsgObj = result.get("aiMessage");
            long messageCount = ((Number)result.get("messageCount")).longValue();
            boolean limitReached = (Boolean)result.get("limitReached");
            long remaining = ((Number)result.get("remaining")).longValue();
            WebSocketMessageResponse userResponse = WebSocketMessageResponse.builder().type(MessageType.USER_MESSAGE).interviewId(interviewId).message(userMsgObj).messageCount(messageCount).limitReached(limitReached).remaining(remaining).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), userResponse);
            WebSocketMessageResponse aiResponse = WebSocketMessageResponse.builder().type(MessageType.AI_MESSAGE).interviewId(interviewId).message(aiMsgObj).messageCount(messageCount).limitReached(limitReached).remaining(remaining).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), aiResponse);
            if (limitReached) {
                WebSocketMessageResponse limitWarning = WebSocketMessageResponse.builder().type(MessageType.LIMIT_REACHED).interviewId(interviewId).message("You have reached the maximum conversation limit. Please upgrade to continue.").messageCount(messageCount).limitReached(true).remaining(0L).timestamp(LocalDateTime.now().toString()).build();
                this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), limitWarning);
            }
        } catch (MessageLimitExceededException e) {
            WebSocketMessageResponse limitResponse = WebSocketMessageResponse.builder().type(MessageType.LIMIT_REACHED).interviewId(interviewId).message(e.getMessage()).limitReached(true).remaining(0L).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), limitResponse);
        } catch (Exception e) {
            log.error("Error processing WS message for interview {}: {}", new Object[]{interviewId, e.getMessage(), e});
            WebSocketMessageResponse errorResponse = WebSocketMessageResponse.builder().type(MessageType.ERROR).interviewId(interviewId).message("Error processing your message: " + e.getMessage()).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), errorResponse);
        }

    }

    @MessageMapping({"/interview/{interviewId}/start"})
    public void startInterview(@DestinationVariable Long interviewId, Principal principal) {
        log.info("WS start interview — interviewId={} user={}", interviewId, principal != null ? principal.getName() : "unknown");

        try {
            Object openingMessage = this.interviewAiService.generateOpeningMessage(interviewId);
            WebSocketMessageResponse response = WebSocketMessageResponse.builder().type(MessageType.AI_MESSAGE).interviewId(interviewId).message(openingMessage).messageCount(1L).limitReached(false).remaining(19L).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), response);
        } catch (Exception e) {
            log.error("Error starting interview {}: {}", interviewId, e.getMessage());
            WebSocketMessageResponse errorResponse = WebSocketMessageResponse.builder().type(MessageType.ERROR).interviewId(interviewId).message("Error starting interview: " + e.getMessage()).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), errorResponse);
        }

    }

    @MessageMapping({"/interview/{interviewId}/finish"})
    public void finishInterview(@DestinationVariable Long interviewId, Principal principal) {
        log.info("WS finish interview — interviewId={} user={}", interviewId, principal != null ? principal.getName() : "unknown");

        try {
            WebSocketMessageResponse evaluatingResponse = WebSocketMessageResponse.builder().type(MessageType.EVALUATING).interviewId(interviewId).message("Interview finished. Generating your evaluation, please wait...").timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), evaluatingResponse);
            Map<String, Object> result = this.interviewAiService.finishInterview(interviewId);
            WebSocketMessageResponse evalResponse = WebSocketMessageResponse.builder().type(MessageType.EVALUATION).interviewId(interviewId).message(result).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), evalResponse);
        } catch (Exception e) {
            log.error("Error finishing interview {}: {}", interviewId, e.getMessage());
            WebSocketMessageResponse errorResponse = WebSocketMessageResponse.builder().type(MessageType.ERROR).interviewId(interviewId).message("Error generating evaluation: " + e.getMessage()).timestamp(LocalDateTime.now().toString()).build();
            this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId), errorResponse);
        }

    }

    @MessageMapping({"/interview/{interviewId}/typing"})
    public void typingIndicator(@DestinationVariable Long interviewId, Principal principal) {
        String username = principal != null ? principal.getName() : "unknown";
        WebSocketMessageResponse typingResponse = WebSocketMessageResponse.builder().type(MessageType.TYPING).interviewId(interviewId).message(username + " is typing...").timestamp(LocalDateTime.now().toString()).build();
        this.messagingTemplate.convertAndSend("/topic/interview/" + String.valueOf(interviewId) + "/typing", typingResponse);
    }
}
