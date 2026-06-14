//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {
    private @NotNull(
            message = "Interview ID is required"
    ) Long interviewId;
    private @NotBlank(
            message = "Message content cannot be empty"
    ) String content;

    public MessageRequest() {
    }

    public MessageRequest(Long interviewId, String content) {
        this.interviewId = interviewId;
        this.content = content;
    }

    public Long getInterviewId() {
        return this.interviewId;
    }

    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static MessageRequestBuilder builder() {
        return new MessageRequestBuilder();
    }

    public static class MessageRequestBuilder {
        private Long interviewId;
        private String content;

        MessageRequestBuilder() {
        }

        public MessageRequestBuilder interviewId(Long interviewId) {
            this.interviewId = interviewId;
            return this;
        }

        public MessageRequestBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MessageRequest build() {
            return new MessageRequest(this.interviewId, this.content);
        }
    }
}
