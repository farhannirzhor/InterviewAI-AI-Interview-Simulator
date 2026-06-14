//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.response.OllamaResponse;
import com.ai_interview_simulator.ai_interview_simulator.exception.OllamaConnectionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class OllamaService {
    private static final Logger log = LoggerFactory.getLogger(OllamaService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;
    @Value("${ollama.model}")
    private String ollamaModel;

    public OllamaService(@Qualifier("ollamaRestTemplate") RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String chat(String systemPrompt, List<Map<String, String>> conversationHistory) {
        try {
            String url = this.ollamaBaseUrl + "/api/chat";
            List<Map<String, String>> messages = new ArrayList();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.addAll(conversationHistory);
            Map<String, Object> requestBody = Map.of("model", this.ollamaModel, "messages", messages, "stream", false, "options", Map.of("temperature", 0.7, "top_p", 0.9, "num_predict", 1024));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity(requestBody, headers);
            log.info("Sending request to Ollama model: {}", this.ollamaModel);
            ResponseEntity<OllamaResponse> response = this.restTemplate.exchange(url, HttpMethod.POST, entity, OllamaResponse.class, new Object[0]);
            if (response.getBody() != null && ((OllamaResponse)response.getBody()).getMessage() != null) {
                String aiReply = ((OllamaResponse)response.getBody()).getMessage().getContent();
                log.info("Ollama response received, length: {}", aiReply.length());
                return aiReply;
            } else {
                log.error("Empty response from Ollama");
                return "I apologize, I could not generate a response. Please try again.";
            }
        } catch (ResourceAccessException e) {
            log.error("Cannot connect to Ollama: {}", e.getMessage());
            throw new OllamaConnectionException();
        } catch (Exception e) {
            log.error("Error calling Ollama chat API: {}", e.getMessage(), e);
            throw new OllamaConnectionException("AI service error: " + e.getMessage(), e);
        }
    }

    public String generate(String prompt) {
        try {
            String url = this.ollamaBaseUrl + "/api/chat";
            List<Map<String, String>> messages = List.of(Map.of("role", "user", "content", prompt));
            Map<String, Object> requestBody = Map.of("model", this.ollamaModel, "messages", messages, "stream", false, "options", Map.of("temperature", 0.3, "top_p", 0.9, "num_predict", 2048));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity(requestBody, headers);
            ResponseEntity<OllamaResponse> response = this.restTemplate.exchange(url, HttpMethod.POST, entity, OllamaResponse.class, new Object[0]);
            return response.getBody() != null && ((OllamaResponse)response.getBody()).getMessage() != null ? ((OllamaResponse)response.getBody()).getMessage().getContent() : "Unable to generate evaluation at this time.";
        } catch (ResourceAccessException e) {
            log.error("Cannot connect to Ollama for generate: {}", e.getMessage());
            throw new OllamaConnectionException();
        } catch (Exception e) {
            log.error("Error calling Ollama generate API: {}", e.getMessage(), e);
            throw new OllamaConnectionException("AI evaluation service error: " + e.getMessage(), e);
        }
    }

    public boolean isOllamaAvailable() {
        try {
            String url = this.ollamaBaseUrl + "/api/tags";
            ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class, new Object[0]);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("Ollama is not available: {}", e.getMessage());
            return false;
        }
    }
}
