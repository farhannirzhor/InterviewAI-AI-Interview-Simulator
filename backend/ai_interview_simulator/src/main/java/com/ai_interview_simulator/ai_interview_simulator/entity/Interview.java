//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
        name = "interviews"
)
public class Interview {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;
    @Column(
            name = "job_role",
            nullable = false,
            length = 200
    )
    private String jobRole;
    @Column(
            name = "job_description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String jobDescription;
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private InterviewStatus status;
    @Column(
            name = "readiness_score"
    )
    private Double readinessScore;
    @Column(
            name = "feedback",
            columnDefinition = "TEXT"
    )
    private String feedback;
    @CreationTimestamp
    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;
    @OneToMany(
            mappedBy = "interview",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    private List<Message> messages;

    public Interview() {
        this.status = Interview.InterviewStatus.ACTIVE;
    }

    public Interview(Long id, User user, String jobRole, String jobDescription, InterviewStatus status, Double readinessScore, String feedback, LocalDateTime createdAt, LocalDateTime updatedAt, List<Message> messages) {
        this.status = Interview.InterviewStatus.ACTIVE;
        this.id = id;
        this.user = user;
        this.jobRole = jobRole;
        this.jobDescription = jobDescription;
        this.status = status;
        this.readinessScore = readinessScore;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public InterviewStatus getStatus() {
        return this.status;
    }

    public void setStatus(InterviewStatus status) {
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

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public static InterviewBuilder builder() {
        return new InterviewBuilder();
    }

    public static class InterviewBuilder {
        private Long id;
        private User user;
        private String jobRole;
        private String jobDescription;
        private InterviewStatus status;
        private Double readinessScore;
        private String feedback;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<Message> messages;

        InterviewBuilder() {
            this.status = Interview.InterviewStatus.ACTIVE;
        }

        public InterviewBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public InterviewBuilder user(User user) {
            this.user = user;
            return this;
        }

        public InterviewBuilder jobRole(String jobRole) {
            this.jobRole = jobRole;
            return this;
        }

        public InterviewBuilder jobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
            return this;
        }

        public InterviewBuilder status(InterviewStatus status) {
            this.status = status;
            return this;
        }

        public InterviewBuilder readinessScore(Double readinessScore) {
            this.readinessScore = readinessScore;
            return this;
        }

        public InterviewBuilder feedback(String feedback) {
            this.feedback = feedback;
            return this;
        }

        public InterviewBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public InterviewBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public InterviewBuilder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Interview build() {
            return new Interview(this.id, this.user, this.jobRole, this.jobDescription, this.status, this.readinessScore, this.feedback, this.createdAt, this.updatedAt, this.messages);
        }
    }

    public static enum InterviewStatus {
        ACTIVE,
        COMPLETED;

        private InterviewStatus() {
        }
    }
}
