//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.entity;

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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(
        name = "messages"
)
public class Message {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "interview_id",
            nullable = false
    )
    private Interview interview;
    @Enumerated(EnumType.STRING)
    @Column(
            name = "sender",
            nullable = false
    )
    private SenderType sender;
    @Column(
            name = "content",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;
    @CreationTimestamp
    @Column(
            name = "timestamp",
            updatable = false
    )
    private LocalDateTime timestamp;

    public Message() {
    }

    public Message(Long id, Interview interview, SenderType sender, String content, LocalDateTime timestamp) {
        this.id = id;
        this.interview = interview;
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

    public Interview getInterview() {
        return this.interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public SenderType getSender() {
        return this.sender;
    }

    public void setSender(SenderType sender) {
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

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public static class MessageBuilder {
        private Long id;
        private Interview interview;
        private SenderType sender;
        private String content;
        private LocalDateTime timestamp;

        MessageBuilder() {
        }

        public MessageBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MessageBuilder interview(Interview interview) {
            this.interview = interview;
            return this;
        }

        public MessageBuilder sender(SenderType sender) {
            this.sender = sender;
            return this;
        }

        public MessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MessageBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Message build() {
            return new Message(this.id, this.interview, this.sender, this.content, this.timestamp);
        }
    }

    public static enum SenderType {
        USER,
        AI;

        private SenderType() {
        }
    }
}
