//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.websocket;

import com.ai_interview_simulator.ai_interview_simulator.security.CustomUserDetailsService;
import com.ai_interview_simulator.ai_interview_simulator.security.JwtUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private static final Logger log = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public WebSocketAuthInterceptor(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = (StompHeaderAccessor)MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        } else {
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String token = this.extractToken(accessor);
                if (token != null) {
                    try {
                        String email = this.jwtUtils.extractUsername(token);
                        if (email != null) {
                            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                            if (this.jwtUtils.isTokenValid(token, userDetails)) {
                                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, (Object)null, userDetails.getAuthorities());
                                accessor.setUser(auth);
                                log.info("WebSocket authenticated: {}", email);
                            } else {
                                log.warn("Invalid JWT token in WebSocket CONNECT");
                            }
                        }
                    } catch (Exception e) {
                        log.error("WebSocket auth error: {}", e.getMessage());
                    }
                } else {
                    log.warn("No JWT token found in WebSocket CONNECT headers");
                }
            }

            return message;
        }
    }

    private String extractToken(StompHeaderAccessor accessor) {
        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = (String)authHeaders.get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }

        List<String> tokenHeaders = accessor.getNativeHeader("token");
        return tokenHeaders != null && !tokenHeaders.isEmpty() ? (String)tokenHeaders.get(0) : null;
    }
}
