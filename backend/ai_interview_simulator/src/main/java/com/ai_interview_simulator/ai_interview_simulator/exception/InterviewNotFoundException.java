//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.exception;

public class InterviewNotFoundException extends RuntimeException {
    public InterviewNotFoundException(String message) {
        super(message);
    }

    public InterviewNotFoundException(Long id) {
        super("Interview not found with id: " + String.valueOf(id));
    }
}
