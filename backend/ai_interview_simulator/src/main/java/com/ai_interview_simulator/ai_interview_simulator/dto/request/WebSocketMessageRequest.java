//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.request;

public class WebSocketMessageRequest {
    private String content;
    private String type;

    public WebSocketMessageRequest() {
    }

    public WebSocketMessageRequest(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static WebSocketMessageRequestBuilder builder() {
        return new WebSocketMessageRequestBuilder();
    }

    public static class WebSocketMessageRequestBuilder {
        private String content;
        private String type;

        WebSocketMessageRequestBuilder() {
        }

        public WebSocketMessageRequestBuilder content(String content) {
            this.content = content;
            return this;
        }

        public WebSocketMessageRequestBuilder type(String type) {
            this.type = type;
            return this;
        }

        public WebSocketMessageRequest build() {
            return new WebSocketMessageRequest(this.content, this.type);
        }
    }
}
