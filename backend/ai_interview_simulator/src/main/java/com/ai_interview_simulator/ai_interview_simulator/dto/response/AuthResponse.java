//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.response;

public class AuthResponse {
    private String token;
    private String tokenType;
    private Long userId;
    private String name;
    private String email;
    private String role;
    private Boolean isPremium;

    public AuthResponse() {
    }

    public AuthResponse(String token, String tokenType, Long userId, String name, String email, String role, Boolean isPremium) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.isPremium = isPremium;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsPremium() {
        return this.isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static AuthResponse of(String token, Long userId, String name, String email, String role, Boolean isPremium) {
        return builder().token(token).tokenType("Bearer").userId(userId).name(name).email(email).role(role).isPremium(isPremium).build();
    }

    public static class AuthResponseBuilder {
        private String token;
        private String tokenType;
        private Long userId;
        private String name;
        private String email;
        private String role;
        private Boolean isPremium;

        AuthResponseBuilder() {
        }

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public AuthResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AuthResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AuthResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public AuthResponseBuilder isPremium(Boolean isPremium) {
            this.isPremium = isPremium;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(this.token, this.tokenType, this.userId, this.name, this.email, this.role, this.isPremium);
        }
    }
}
