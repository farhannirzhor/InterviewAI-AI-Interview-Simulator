//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.controller;

import com.ai_interview_simulator.ai_interview_simulator.dto.response.ApiResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.UserResponse;
import com.ai_interview_simulator.ai_interview_simulator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/users"})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/me"})
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse response = this.userService.getCurrentUserProfile();
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved", response));
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = this.userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved", response));
    }
}
