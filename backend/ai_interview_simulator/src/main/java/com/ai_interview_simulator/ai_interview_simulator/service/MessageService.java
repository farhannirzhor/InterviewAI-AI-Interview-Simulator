//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.response.MessageResponse;
import com.ai_interview_simulator.ai_interview_simulator.entity.Interview;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message.SenderType;
import com.ai_interview_simulator.ai_interview_simulator.repository.MessageRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Message saveUserMessage(Interview interview, String content) {
        Message message = Message.builder().interview(interview).sender(SenderType.USER).content(content).build();
        Message saved = (Message)this.messageRepository.save(message);
        log.info("User message saved for interview id={}", interview.getId());
        return saved;
    }

    @Transactional
    public Message saveAiMessage(Interview interview, String content) {
        Message message = Message.builder().interview(interview).sender(SenderType.AI).content(content).build();
        Message saved = (Message)this.messageRepository.save(message);
        log.info("AI message saved for interview id={}", interview.getId());
        return saved;
    }

    @Transactional(
            readOnly = true
    )
    public List<Message> getMessages(Interview interview) {
        return this.messageRepository.findByInterviewOrderByTimestampAsc(interview);
    }

    @Transactional(
            readOnly = true
    )
    public List<MessageResponse> getMessageResponses(Interview interview) {
        return (List)this.messageRepository.findByInterviewOrderByTimestampAsc(interview).stream().map(MessageResponse::fromEntity).collect(Collectors.toList());
    }

    @Transactional(
            readOnly = true
    )
    public List<Map<String, String>> buildConversationHistory(Interview interview) {
        List<Message> messages = this.messageRepository.findByInterviewOrderByTimestampAsc(interview);
        return (List)messages.stream().map((msg) -> Map.of("role", msg.getSender() == SenderType.USER ? "user" : "assistant", "content", msg.getContent())).collect(Collectors.toList());
    }

    @Transactional(
            readOnly = true
    )
    public long countMessages(Interview interview) {
        return this.messageRepository.countByInterview(interview);
    }

    @Transactional(
            readOnly = true
    )
    public long countUserMessages(Interview interview) {
        return this.messageRepository.countUserMessagesByInterview(interview);
    }
}
