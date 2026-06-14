//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.response;

import com.ai_interview_simulator.ai_interview_simulator.entity.Interview;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InterviewResponse {
    private Long id;
    private Long userId;
    private String jobRole;
    private String jobDescription;
    private String status;
    private Double readinessScore;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MessageResponse> messages;
    private long messageCount;
    private boolean limitReached;

    public InterviewResponse() {
    }

    public InterviewResponse(Long id, Long userId, String jobRole, String jobDescription, String status, Double readinessScore, String feedback, LocalDateTime createdAt, LocalDateTime updatedAt, List<MessageResponse> messages, long messageCount, boolean limitReached) {
        this.id = id;
        this.userId = userId;
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
        this.status = status;
        this.readinessScore = readinessScore;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages;
        this.messageCount = messageCount;
        this.limitReached = limitReached;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getJobRole() {
        return this.jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobDescription() {
        return this.jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getReadinessScore() {
        return this.readinessScore;
    }

    public void setReadinessScore(Double readinessScore) {
        this.readinessScore = readinessScore;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MessageResponse> getMessages() {
        return this.messages;
    }

    public void setMessages(List<MessageResponse> messages) {
        this.messages = messages;
    }

    public long getMessageCount() {
        return this.messageCount;
    }

    public void setMessageCount(long messageCount) {
        this.messageCount = messageCount;
    }

    public boolean isLimitReached() {
        return this.limitReached;
    }

    public void setLimitReached(boolean limitReached) {
        this.limitReached = limitReached;
    }

    public static InterviewResponseBuilder builder() {
        return new InterviewResponseBuilder();
    }

    public static InterviewResponse fromEntity(Interview interview) {
        return builder().id(interview.getId()).userId(interview.getUser().getId()).jobRole(interview.getJobRole()).jobDescription(interview.getJobDescription()).status(interview.getStatus().name()).readinessScore(interview.getReadinessScore()).feedback(interview.getFeedback()).createdAt(interview.getCreatedAt()).updatedAt(interview.getUpdatedAt()).build();
    }

    public static InterviewResponse fromEntityWithMessages(Interview interview, List<Message> messages, long messageCount, int messageLimit) {
        List<MessageResponse> messageResponses = (List)messages.stream().map(MessageResponse::fromEntity).collect(Collectors.toList());
        return builder().id(interview.getId()).userId(interview.getUser().getId()).jobRole(interview.getJobRole()).jobDescription(interview.getJobDescription()).status(interview.getStatus().name()).readinessScore(interview.getReadinessScore()).feedback(interview.getFeedback()).createdAt(interview.getCreatedAt()).updatedAt(interview.getUpdatedAt()).messages(messageResponses).messageCount(messageCount).limitReached(messageCount >= (long)messageLimit).build();
    }

    public static class InterviewResponseBuilder {
        private Long id;
        private Long userId;
        private String jobRole;
        private String jobDescription;
        private String status;
        private Double readinessScore;
        private String feedback;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<MessageResponse> messages;
        private long messageCount;
        private boolean limitReached;

        InterviewResponseBuilder() {
        }

        public InterviewResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public InterviewResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public InterviewResponseBuilder jobRole(String jobRole) {
            this.jobRole = jobRole;
            return this;
        }

        public InterviewResponseBuilder jobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
            return this;
        }

        public InterviewResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public InterviewResponseBuilder readinessScore(Double readinessScore) {
            this.readinessScore = readinessScore;
            return this;
        }

        public InterviewResponseBuilder feedback(String feedback) {
            this.feedback = feedback;
            return this;
        }

        public InterviewResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public InterviewResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public InterviewResponseBuilder messages(List<MessageResponse> messages) {
            this.messages = messages;
            return this;
        }

        public InterviewResponseBuilder messageCount(long messageCount) {
            this.messageCount = messageCount;
            return this;
        }

        public InterviewResponseBuilder limitReached(boolean limitReached) {
            this.limitReached = limitReached;
            return this;
        }

        public InterviewResponse build() {
            return new InterviewResponse(this.id, this.userId, this.jobRole, this.jobDescription, this.status, this.readinessScore, this.feedback, this.createdAt, this.updatedAt, this.messages, this.messageCount, this.limitReached);
        }
    }
}
