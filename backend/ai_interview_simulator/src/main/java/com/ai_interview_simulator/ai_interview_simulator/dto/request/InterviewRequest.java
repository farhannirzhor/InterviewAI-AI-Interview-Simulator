//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InterviewRequest {
    private @NotBlank(
            message = "Job role is required"
    ) @Size(
            min = 2,
            max = 200,
            message = "Job role must be between 2 and 200 characters"
    ) String jobRole;
    private @NotBlank(
            message = "Job description is required"
    ) @Size(
            min = 10,
            message = "Job description must be at least 10 characters"
    ) String jobDescription;

    public InterviewRequest() {
    }

    public InterviewRequest(String jobRole, String jobDescription) {
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
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

    public static InterviewRequestBuilder builder() {
        return new InterviewRequestBuilder();
    }

    public static class InterviewRequestBuilder {
        private String jobRole;
        private String jobDescription;

        InterviewRequestBuilder() {
        }

        public InterviewRequestBuilder jobRole(String jobRole) {
            this.jobRole = jobRole;
            return this;
        }

        public InterviewRequestBuilder jobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
            return this;
        }

        public InterviewRequest build() {
            return new InterviewRequest(this.jobRole, this.jobDescription);
        }
    }
}
