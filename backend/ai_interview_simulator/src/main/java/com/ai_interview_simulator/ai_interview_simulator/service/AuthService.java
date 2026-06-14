//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.request.LoginRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.request.RegisterRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.AuthResponse;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import com.ai_interview_simulator.ai_interview_simulator.entity.User.Role;
import com.ai_interview_simulator.ai_interview_simulator.exception.InvalidCredentialsException;
import com.ai_interview_simulator.ai_interview_simulator.exception.UserAlreadyExistsException;
import com.ai_interview_simulator.ai_interview_simulator.repository.UserRepository;
import com.ai_interview_simulator.ai_interview_simulator.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered: " + request.getEmail());
        } else {
            User user = User.builder().name(request.getName()).email(request.getEmail()).password(this.passwordEncoder.encode(request.getPassword())).role(Role.USER).isPremium(false).build();
            User savedUser = (User)this.userRepository.save(user);
            log.info("New user registered: {}", savedUser.getEmail());
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(savedUser.getEmail());
            String token = this.jwtUtils.generateToken(userDetails);
            return AuthResponse.of(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole().name(), savedUser.getIsPremium());
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException var5) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = (User)this.userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidCredentialsException("User not found"));
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getEmail());
        String token = this.jwtUtils.generateToken(userDetails);
        log.info("User logged in: {}", user.getEmail());
        return AuthResponse.of(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name(), user.getIsPremium());
    }
}
