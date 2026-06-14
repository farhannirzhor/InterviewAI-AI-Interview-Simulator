//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    private @NotBlank(
            message = "Email is required"
    ) @Email(
            message = "Please provide a valid email address"
    ) String email;
    private @NotBlank(
            message = "Password is required"
    ) String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginRequestBuilder builder() {
        return new LoginRequestBuilder();
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

    public static class LoginRequestBuilder {
        private String email;
        private String password;

        LoginRequestBuilder() {
        }

        public LoginRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public LoginRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public LoginRequest build() {
            return new LoginRequest(this.email, this.password);
        }

        public String toString() {
            return "LoginRequest.LoginRequestBuilder(email=" + this.email + ", password=" + this.password + ")";
        }
    }
}
