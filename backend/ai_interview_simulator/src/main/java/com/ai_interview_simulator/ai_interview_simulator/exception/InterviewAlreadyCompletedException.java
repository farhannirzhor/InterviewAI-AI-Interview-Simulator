//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.exception;

public class InterviewAlreadyCompletedException extends RuntimeException {
    public InterviewAlreadyCompletedException(String message) {
        super(message);
    }

    public InterviewAlreadyCompletedException(Long interviewId) {
        super("Interview with id " + String.valueOf(interviewId) + " has already been completed. You cannot send more messages to a completed interview.");
    }
}
