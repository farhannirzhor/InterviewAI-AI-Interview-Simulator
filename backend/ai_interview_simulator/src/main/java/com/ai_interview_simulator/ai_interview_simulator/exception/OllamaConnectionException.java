//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.exception;

public class OllamaConnectionException extends RuntimeException {
    public OllamaConnectionException(String message) {
        super(message);
    }

    public OllamaConnectionException() {
        super("Could not connect to the Ollama AI service. Please ensure Ollama is running locally on port 11434 and the gemma3 model is installed.");
    }

    public OllamaConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
