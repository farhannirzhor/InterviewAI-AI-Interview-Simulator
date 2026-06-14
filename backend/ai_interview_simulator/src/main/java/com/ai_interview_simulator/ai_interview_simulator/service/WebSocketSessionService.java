//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSessionService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketSessionService.class);
    private final Map<String, String> activeSessions = new ConcurrentHashMap();
    private final Map<Long, Set<String>> interviewRooms = new ConcurrentHashMap();

    public WebSocketSessionService() {
    }

    public void addSession(String sessionId, String email) {
        this.activeSessions.put(sessionId, email);
        log.info("Session added: {} -> {}", sessionId, email);
    }

    public void removeSession(String sessionId) {
        String email = (String)this.activeSessions.remove(sessionId);
        if (email != null) {
            this.interviewRooms.values().forEach((users) -> users.remove(email));
            log.info("Session removed: {} ({})", sessionId, email);
        }

    }

    public String getUserBySession(String sessionId) {
        return (String)this.activeSessions.getOrDefault(sessionId, "unknown");
    }

    public void joinRoom(Long interviewId, String email) {
        ((Set)this.interviewRooms.computeIfAbsent(interviewId, (k) -> ConcurrentHashMap.newKeySet())).add(email);
        log.info("User {} joined interview room {}", email, interviewId);
    }

    public void leaveRoom(Long interviewId, String email) {
        Set<String> users = (Set)this.interviewRooms.get(interviewId);
        if (users != null) {
            users.remove(email);
            log.info("User {} left interview room {}", email, interviewId);
        }

    }

    public Set<String> getRoomUsers(Long interviewId) {
        return (Set)this.interviewRooms.getOrDefault(interviewId, ConcurrentHashMap.newKeySet());
    }

    public boolean isUserInRoom(Long interviewId, String email) {
        Set<String> users = (Set)this.interviewRooms.get(interviewId);
        return users != null && users.contains(email);
    }

    public int getActiveSessionCount() {
        return this.activeSessions.size();
    }
}
