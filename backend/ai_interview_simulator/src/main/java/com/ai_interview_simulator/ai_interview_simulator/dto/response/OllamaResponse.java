//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class OllamaResponse {
    @JsonProperty("model")
    private String model;
    @JsonProperty("message")
    private OllamaMessage message;
    @JsonProperty("done")
    private Boolean done;
    @JsonProperty("total_duration")
    private Long totalDuration;

    public OllamaResponse() {
    }

    public OllamaResponse(String model, OllamaMessage message, Boolean done, Long totalDuration) {
        this.model = model;
        this.message = message;
        this.done = done;
        this.totalDuration = totalDuration;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public OllamaMessage getMessage() {
        return this.message;
    }

    public void setMessage(OllamaMessage message) {
        this.message = message;
    }

    public Boolean getDone() {
        return this.done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Long getTotalDuration() {
        return this.totalDuration;
    }

    public void setTotalDuration(Long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public static OllamaResponseBuilder builder() {
        return new OllamaResponseBuilder();
    }

    public static class OllamaResponseBuilder {
        private String model;
        private OllamaMessage message;
        private Boolean done;
        private Long totalDuration;

        OllamaResponseBuilder() {
        }

        public OllamaResponseBuilder model(String model) {
            this.model = model;
            return this;
        }

        public OllamaResponseBuilder message(OllamaMessage message) {
            this.message = message;
            return this;
        }

        public OllamaResponseBuilder done(Boolean done) {
            this.done = done;
            return this;
        }

        public OllamaResponseBuilder totalDuration(Long totalDuration) {
            this.totalDuration = totalDuration;
            return this;
        }

        public OllamaResponse build() {
            return new OllamaResponse(this.model, this.message, this.done, this.totalDuration);
        }
    }

    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static class OllamaMessage {
        @JsonProperty("role")
        private String role;
        @JsonProperty("content")
        private String content;

        public OllamaMessage() {
        }

        public OllamaMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return this.role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
