//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.response;

import java.util.List;

public class EvaluationResponse {
    private Double readinessScore;
    private String summary;
    private List<String> strengths;
    private List<String> areasForImprovement;
    private String detailedFeedback;
    private String recommendation;
    private ScoreBreakdown scoreBreakdown;
    private Long interviewId;
    private String jobRole;
    private String jobDescription;
    private Long totalMessages;
    private String interviewStatus;
    private String completedAt;
    private String rawEvaluation;
    private String readinessLabel;

    public EvaluationResponse() {
    }

    public EvaluationResponse(Double readinessScore, String summary, List<String> strengths, List<String> areasForImprovement, String detailedFeedback, String recommendation, ScoreBreakdown scoreBreakdown, Long interviewId, String jobRole, String jobDescription, Long totalMessages, String interviewStatus, String completedAt, String rawEvaluation, String readinessLabel) {
        this.readinessScore = readinessScore;
        this.summary = summary;
        this.strengths = strengths;
        this.areasForImprovement = areasForImprovement;
        this.detailedFeedback = detailedFeedback;
        this.recommendation = recommendation;
        this.scoreBreakdown = scoreBreakdown;
        this.interviewId = interviewId;
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
        this.totalMessages = totalMessages;
        this.interviewStatus = interviewStatus;
        this.completedAt = completedAt;
        this.rawEvaluation = rawEvaluation;
        this.readinessLabel = readinessLabel;
    }

    public Double getReadinessScore() {
        return this.readinessScore;
    }

    public void setReadinessScore(Double readinessScore) {
        this.readinessScore = readinessScore;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getStrengths() {
        return this.strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getAreasForImprovement() {
        return this.areasForImprovement;
    }

    public void setAreasForImprovement(List<String> areasForImprovement) {
        this.areasForImprovement = areasForImprovement;
    }

    public String getDetailedFeedback() {
        return this.detailedFeedback;
    }

    public void setDetailedFeedback(String detailedFeedback) {
        this.detailedFeedback = detailedFeedback;
    }

    public String getRecommendation() {
        return this.recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public ScoreBreakdown getScoreBreakdown() {
        return this.scoreBreakdown;
    }

    public void setScoreBreakdown(ScoreBreakdown scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public Long getInterviewId() {
        return this.interviewId;
    }

    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
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

    public Long getTotalMessages() {
        return this.totalMessages;
    }

    public void setTotalMessages(Long totalMessages) {
        this.totalMessages = totalMessages;
    }

    public String getInterviewStatus() {
        return this.interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    public String getCompletedAt() {
        return this.completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getRawEvaluation() {
        return this.rawEvaluation;
    }

    public void setRawEvaluation(String rawEvaluation) {
        this.rawEvaluation = rawEvaluation;
    }

    public String getReadinessLabel() {
        return this.readinessLabel;
    }

    public void setReadinessLabel(String readinessLabel) {
        this.readinessLabel = readinessLabel;
    }

    public static EvaluationResponseBuilder builder() {
        return new EvaluationResponseBuilder();
    }

    public static String buildReadinessLabel(Double score) {
        if (score == null) {
            return "Unknown";
        } else if (score >= (double)85.0F) {
            return "Highly Ready";
        } else if (score >= (double)70.0F) {
            return "Ready";
        } else if (score >= (double)55.0F) {
            return "Moderately Ready";
        } else {
            return score >= (double)40.0F ? "Needs Improvement" : "Not Ready";
        }
    }

    public static class EvaluationResponseBuilder {
        private Double readinessScore;
        private String summary;
        private List<String> strengths;
        private List<String> areasForImprovement;
        private String detailedFeedback;
        private String recommendation;
        private ScoreBreakdown scoreBreakdown;
        private Long interviewId;
        private String jobRole;
        private String jobDescription;
        private Long totalMessages;
        private String interviewStatus;
        private String completedAt;
        private String rawEvaluation;
        private String readinessLabel;

        EvaluationResponseBuilder() {
        }

        public EvaluationResponseBuilder readinessScore(Double readinessScore) {
            this.readinessScore = readinessScore;
            return this;
        }

        public EvaluationResponseBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public EvaluationResponseBuilder strengths(List<String> strengths) {
            this.strengths = strengths;
            return this;
        }

        public EvaluationResponseBuilder areasForImprovement(List<String> areasForImprovement) {
            this.areasForImprovement = areasForImprovement;
            return this;
        }

        public EvaluationResponseBuilder detailedFeedback(String detailedFeedback) {
            this.detailedFeedback = detailedFeedback;
            return this;
        }

        public EvaluationResponseBuilder recommendation(String recommendation) {
            this.recommendation = recommendation;
            return this;
        }

        public EvaluationResponseBuilder scoreBreakdown(ScoreBreakdown scoreBreakdown) {
            this.scoreBreakdown = scoreBreakdown;
            return this;
        }

        public EvaluationResponseBuilder interviewId(Long interviewId) {
            this.interviewId = interviewId;
            return this;
        }

        public EvaluationResponseBuilder jobRole(String jobRole) {
            this.jobRole = jobRole;
            return this;
        }

        public EvaluationResponseBuilder jobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
            return this;
        }

        public EvaluationResponseBuilder totalMessages(Long totalMessages) {
            this.totalMessages = totalMessages;
            return this;
        }

        public EvaluationResponseBuilder interviewStatus(String interviewStatus) {
            this.interviewStatus = interviewStatus;
            return this;
        }

        public EvaluationResponseBuilder completedAt(String completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public EvaluationResponseBuilder rawEvaluation(String rawEvaluation) {
            this.rawEvaluation = rawEvaluation;
            return this;
        }

        public EvaluationResponseBuilder readinessLabel(String readinessLabel) {
            this.readinessLabel = readinessLabel;
            return this;
        }

        public EvaluationResponse build() {
            return new EvaluationResponse(this.readinessScore, this.summary, this.strengths, this.areasForImprovement, this.detailedFeedback, this.recommendation, this.scoreBreakdown, this.interviewId, this.jobRole, this.jobDescription, this.totalMessages, this.interviewStatus, this.completedAt, this.rawEvaluation, this.readinessLabel);
        }
    }

    public static class ScoreBreakdown {
        private Double technicalKnowledge;
        private Double communicationSkills;
        private Double problemSolving;
        private Double relevantExperience;
        private Double culturalFitAttitude;

        public ScoreBreakdown() {
        }

        public ScoreBreakdown(Double technicalKnowledge, Double communicationSkills, Double problemSolving, Double relevantExperience, Double culturalFitAttitude) {
            this.technicalKnowledge = technicalKnowledge;
            this.communicationSkills = communicationSkills;
            this.problemSolving = problemSolving;
            this.relevantExperience = relevantExperience;
            this.culturalFitAttitude = culturalFitAttitude;
        }

        public Double getTechnicalKnowledge() {
            return this.technicalKnowledge;
        }

        public void setTechnicalKnowledge(Double technicalKnowledge) {
            this.technicalKnowledge = technicalKnowledge;
        }

        public Double getCommunicationSkills() {
            return this.communicationSkills;
        }

        public void setCommunicationSkills(Double communicationSkills) {
            this.communicationSkills = communicationSkills;
        }

        public Double getProblemSolving() {
            return this.problemSolving;
        }

        public void setProblemSolving(Double problemSolving) {
            this.problemSolving = problemSolving;
        }

        public Double getRelevantExperience() {
            return this.relevantExperience;
        }

        public void setRelevantExperience(Double relevantExperience) {
            this.relevantExperience = relevantExperience;
        }

        public Double getCulturalFitAttitude() {
            return this.culturalFitAttitude;
        }

        public void setCulturalFitAttitude(Double culturalFitAttitude) {
            this.culturalFitAttitude = culturalFitAttitude;
        }

        public static ScoreBreakdownBuilder builder() {
            return new ScoreBreakdownBuilder();
        }

        public static class ScoreBreakdownBuilder {
            private Double technicalKnowledge;
            private Double communicationSkills;
            private Double problemSolving;
            private Double relevantExperience;
            private Double culturalFitAttitude;

            ScoreBreakdownBuilder() {
            }

            public ScoreBreakdownBuilder technicalKnowledge(Double technicalKnowledge) {
                this.technicalKnowledge = technicalKnowledge;
                return this;
            }

            public ScoreBreakdownBuilder communicationSkills(Double communicationSkills) {
                this.communicationSkills = communicationSkills;
                return this;
            }

            public ScoreBreakdownBuilder problemSolving(Double problemSolving) {
                this.problemSolving = problemSolving;
                return this;
            }

            public ScoreBreakdownBuilder relevantExperience(Double relevantExperience) {
                this.relevantExperience = relevantExperience;
                return this;
            }

            public ScoreBreakdownBuilder culturalFitAttitude(Double culturalFitAttitude) {
                this.culturalFitAttitude = culturalFitAttitude;
                return this;
            }

            public ScoreBreakdown build() {
                return new ScoreBreakdown(this.technicalKnowledge, this.communicationSkills, this.problemSolving, this.relevantExperience, this.culturalFitAttitude);
            }
        }
    }
}
