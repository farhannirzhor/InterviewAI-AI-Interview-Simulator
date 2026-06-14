//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OllamaConfig {
    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;
    @Value("${ollama.model}")
    private String ollamaModel;

    public OllamaConfig() {
    }

    @Bean(
            name = {"ollamaRestTemplate"}
    )
    public RestTemplate ollamaRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(120000);
        factory.setReadTimeout(300000);
        return new RestTemplate(factory);
    }
}
