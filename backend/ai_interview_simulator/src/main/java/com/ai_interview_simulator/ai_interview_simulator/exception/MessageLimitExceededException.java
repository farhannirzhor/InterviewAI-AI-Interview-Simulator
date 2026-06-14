//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.exception;

public class MessageLimitExceededException extends RuntimeException {
    public MessageLimitExceededException(String message) {
        super(message);
    }

    public MessageLimitExceededException() {
        super("You have reached the maximum conversation limit of 20 messages. Please upgrade to continue.");
    }
}
