//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.response;

import com.ai_interview_simulator.ai_interview_simulator.entity.Message;
import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private Long interviewId;
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public MessageResponse() {
    }

    public MessageResponse(Long id, Long interviewId, String sender, String content, LocalDateTime timestamp) {
        this.id = id;
        this.interviewId = interviewId;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInterviewId() {
        return this.interviewId;
    }

    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static MessageResponseBuilder builder() {
        return new MessageResponseBuilder();
    }

    public static MessageResponse fromEntity(Message message) {
        return builder().id(message.getId()).interviewId(message.getInterview().getId()).sender(message.getSender().name()).content(message.getContent()).timestamp(message.getTimestamp()).build();
    }

    public static class MessageResponseBuilder {
        private Long id;
        private Long interviewId;
        private String sender;
        private String content;
        private LocalDateTime timestamp;

        MessageResponseBuilder() {
        }

        public MessageResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MessageResponseBuilder interviewId(Long interviewId) {
            this.interviewId = interviewId;
            return this;
        }

        public MessageResponseBuilder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public MessageResponseBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MessageResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MessageResponse build() {
            return new MessageResponse(this.id, this.interviewId, this.sender, this.content, this.timestamp);
        }
    }
}
