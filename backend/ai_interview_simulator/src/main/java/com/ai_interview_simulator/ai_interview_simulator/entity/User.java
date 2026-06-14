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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
        name = "users"
)
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;
    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 150
    )
    private String email;
    @Column(
            name = "password",
            nullable = false,
            length = 255
    )
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false
    )
    private Role role;
    @Column(
            name = "is_premium",
            nullable = false
    )
    private Boolean isPremium;
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
            mappedBy = "user",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    private List<Interview> interviews;
    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    private List<Payment> payments;

    public User() {
        this.role = User.Role.USER;
        this.isPremium = false;
    }

    public User(Long id, String name, String email, String password, Role role, Boolean isPremium, LocalDateTime createdAt, LocalDateTime updatedAt, List<Interview> interviews, List<Payment> payments) {
        this.role = User.Role.USER;
        this.isPremium = false;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isPremium = isPremium;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.interviews = interviews;
        this.payments = payments;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsPremium() {
        return this.isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
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

    public List<Interview> getInterviews() {
        return this.interviews;
    }

    public void setInterviews(List<Interview> interviews) {
        this.interviews = interviews;
    }

    public List<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public static enum Role {
        USER,
        ADMIN;

        private Role() {
        }
    }

    public static class UserBuilder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private Role role;
        private Boolean isPremium;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<Interview> interviews;
        private List<Payment> payments;

        UserBuilder() {
            this.role = User.Role.USER;
            this.isPremium = false;
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder isPremium(Boolean isPremium) {
            this.isPremium = isPremium;
            return this;
        }

        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserBuilder interviews(List<Interview> interviews) {
            this.interviews = interviews;
            return this;
        }

        public UserBuilder payments(List<Payment> payments) {
            this.payments = payments;
            return this;
        }

        public User build() {
            return new User(this.id, this.name, this.email, this.password, this.role, this.isPremium, this.createdAt, this.updatedAt, this.interviews, this.payments);
        }

        public String toString() {
            String var10000 = String.valueOf(this.id);
            return "User.UserBuilder(id=" + var10000 + ", name=" + this.name + ", email=" + this.email + ", password=" + this.password + ", role=" + String.valueOf(this.role) + ", isPremium=" + String.valueOf(this.isPremium) + ", createdAt=" + String.valueOf(this.createdAt) + ", updatedAt=" + String.valueOf(this.updatedAt) + ", interviews=" + String.valueOf(this.interviews) + ", payments=" + String.valueOf(this.payments) + ")";
        }
    }
}
