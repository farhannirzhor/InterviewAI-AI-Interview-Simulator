//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.request.InterviewRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.InterviewResponse;
import com.ai_interview_simulator.ai_interview_simulator.entity.Interview;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import com.ai_interview_simulator.ai_interview_simulator.entity.Interview.InterviewStatus;
import com.ai_interview_simulator.ai_interview_simulator.exception.InterviewNotFoundException;
import com.ai_interview_simulator.ai_interview_simulator.exception.MessageLimitExceededException;
import com.ai_interview_simulator.ai_interview_simulator.repository.InterviewRepository;
import com.ai_interview_simulator.ai_interview_simulator.repository.MessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewService {
    private static final Logger log = LoggerFactory.getLogger(InterviewService.class);
    private final InterviewRepository interviewRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    @Value("${interview.message.limit}")
    private int messageLimit;

    public InterviewService(InterviewRepository interviewRepository, MessageRepository messageRepository, UserService userService) {
        this.interviewRepository = interviewRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Transactional
    public InterviewResponse createInterview(InterviewRequest request) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = Interview.builder().user(currentUser).jobRole(request.getJobRole()).jobDescription(request.getJobDescription()).status(InterviewStatus.ACTIVE).build();
        Interview saved = (Interview)this.interviewRepository.save(interview);
        log.info("Interview created: id={} for user={}", saved.getId(), currentUser.getEmail());
        return InterviewResponse.fromEntity(saved);
    }

    @Transactional(
            readOnly = true
    )
    public InterviewResponse getInterviewById(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        List<Message> messages = this.messageRepository.findByInterviewOrderByTimestampAsc(interview);
        long messageCount = this.messageRepository.countByInterview(interview);
        return InterviewResponse.fromEntityWithMessages(interview, messages, messageCount, this.messageLimit);
    }

    @Transactional(
            readOnly = true
    )
    public List<InterviewResponse> getMyInterviews() {
        User currentUser = this.userService.getCurrentUser();
        return (List)this.interviewRepository.findByUserOrderByCreatedAtDesc(currentUser).stream().map(InterviewResponse::fromEntity).collect(Collectors.toList());
    }

    @Transactional(
            readOnly = true
    )
    public long getMessageCount(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        return this.messageRepository.countByInterview(interview);
    }

    @Transactional(
            readOnly = true
    )
    public boolean isLimitReached(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        if (currentUser.getIsPremium()) {
            return false;
        } else {
            long count = this.messageRepository.countByInterview(interview);
            return count >= (long)this.messageLimit;
        }
    }

    @Transactional(
            readOnly = true
    )
    public void enforceMessageLimit(Interview interview, User user) {
        if (!user.getIsPremium()) {
            long count = this.messageRepository.countByInterview(interview);
            if (count >= (long)this.messageLimit) {
                log.warn("Message limit reached for interview id={} user={}", interview.getId(), user.getEmail());
                throw new MessageLimitExceededException();
            }
        }
    }

    @Transactional(
            readOnly = true
    )
    public Interview getInterviewEntity(Long interviewId, User user) {
        return (Interview)this.interviewRepository.findByIdAndUser(interviewId, user).orElseThrow(() -> new InterviewNotFoundException(interviewId));
    }

    @Transactional
    public void completeInterview(Interview interview) {
        interview.setStatus(InterviewStatus.COMPLETED);
        this.interviewRepository.save(interview);
        log.info("Interview completed: id={}", interview.getId());
    }

    @Transactional
    public InterviewResponse saveScore(Long interviewId, Double score, String feedback) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        interview.setReadinessScore(score);
        interview.setFeedback(feedback);
        interview.setStatus(InterviewStatus.COMPLETED);
        Interview saved = (Interview)this.interviewRepository.save(interview);
        log.info("Score saved for interview id={}: score={}", interviewId, score);
        List<Message> messages = this.messageRepository.findByInterviewOrderByTimestampAsc(saved);
        long count = this.messageRepository.countByInterview(saved);
        return InterviewResponse.fromEntityWithMessages(saved, messages, count, this.messageLimit);
    }

    @Transactional
    public void deleteInterview(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        this.interviewRepository.delete(interview);
        log.info("Interview deleted: id={} by user={}", interviewId, currentUser.getEmail());
    }
}
