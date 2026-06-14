//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.response;

public class WebSocketMessageResponse {
    private MessageType type;
    private Long interviewId;
    private Object message;
    private Long messageCount;
    private Boolean limitReached;
    private Long remaining;
    private String timestamp;

    public WebSocketMessageResponse() {
    }

    public WebSocketMessageResponse(MessageType type, Long interviewId, Object message, Long messageCount, Boolean limitReached, Long remaining, String timestamp) {
        this.type = type;
        this.interviewId = interviewId;
        this.message = message;
        this.messageCount = messageCount;
        this.limitReached = limitReached;
        this.remaining = remaining;
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return this.type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Long getInterviewId() {
        return this.interviewId;
    }

    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
    }

    public Object getMessage() {
        return this.message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Long getMessageCount() {
        return this.messageCount;
    }

    public void setMessageCount(Long messageCount) {
        this.messageCount = messageCount;
    }

    public Boolean getLimitReached() {
        return this.limitReached;
    }

    public void setLimitReached(Boolean limitReached) {
        this.limitReached = limitReached;
    }

    public Long getRemaining() {
        return this.remaining;
    }

    public void setRemaining(Long remaining) {
        this.remaining = remaining;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static WebSocketMessageResponseBuilder builder() {
        return new WebSocketMessageResponseBuilder();
    }

    public static enum MessageType {
        USER_MESSAGE,
        AI_MESSAGE,
        EVALUATION,
        EVALUATING,
        LIMIT_REACHED,
        TYPING,
        ERROR,
        SYSTEM;

        private MessageType() {
        }
    }

    public static class WebSocketMessageResponseBuilder {
        private MessageType type;
        private Long interviewId;
        private Object message;
        private Long messageCount;
        private Boolean limitReached;
        private Long remaining;
        private String timestamp;

        WebSocketMessageResponseBuilder() {
        }

        public WebSocketMessageResponseBuilder type(MessageType type) {
            this.type = type;
            return this;
        }

        public WebSocketMessageResponseBuilder interviewId(Long interviewId) {
            this.interviewId = interviewId;
            return this;
        }

        public WebSocketMessageResponseBuilder message(Object message) {
            this.message = message;
            return this;
        }

        public WebSocketMessageResponseBuilder messageCount(Long messageCount) {
            this.messageCount = messageCount;
            return this;
        }

        public WebSocketMessageResponseBuilder limitReached(Boolean limitReached) {
            this.limitReached = limitReached;
            return this;
        }

        public WebSocketMessageResponseBuilder remaining(Long remaining) {
            this.remaining = remaining;
            return this;
        }

        public WebSocketMessageResponseBuilder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public WebSocketMessageResponse build() {
            return new WebSocketMessageResponse(this.type, this.interviewId, this.message, this.messageCount, this.limitReached, this.remaining, this.timestamp);
        }
    }
}
