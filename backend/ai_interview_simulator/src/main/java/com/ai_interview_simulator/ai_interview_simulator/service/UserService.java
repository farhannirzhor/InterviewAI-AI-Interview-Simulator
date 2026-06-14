//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.response.UserResponse;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import com.ai_interview_simulator.ai_interview_simulator.exception.UnauthorizedException;
import com.ai_interview_simulator.ai_interview_simulator.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return (User)this.userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("User not found: " + email));
        } else {
            throw new UnauthorizedException("No authenticated user found");
        }
    }

    public UserResponse getCurrentUserProfile() {
        User user = this.getCurrentUser();
        return UserResponse.fromEntity(user);
    }

    public UserResponse getUserById(Long id) {
        User currentUser = this.getCurrentUser();
        if (!currentUser.getId().equals(id)) {
            throw new UnauthorizedException("You are not authorized to view this profile");
        } else {
            return UserResponse.fromEntity(currentUser);
        }
    }
}
