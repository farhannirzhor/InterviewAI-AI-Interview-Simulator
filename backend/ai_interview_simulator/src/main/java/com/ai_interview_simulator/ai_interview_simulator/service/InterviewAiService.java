//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.response.EvaluationResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.MessageResponse;
import com.ai_interview_simulator.ai_interview_simulator.entity.Interview;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import com.ai_interview_simulator.ai_interview_simulator.entity.Interview.InterviewStatus;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message.SenderType;
import com.ai_interview_simulator.ai_interview_simulator.exception.InterviewAlreadyCompletedException;
import com.ai_interview_simulator.ai_interview_simulator.repository.InterviewRepository;
import com.ai_interview_simulator.ai_interview_simulator.repository.MessageRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewAiService {
    private static final Logger log = LoggerFactory.getLogger(InterviewAiService.class);
    private final OllamaService ollamaService;
    private final MessageService messageService;
    private final InterviewService interviewService;
    private final InterviewRepository interviewRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final EvaluationService evaluationService;
    @Value("${interview.message.limit}")
    private int messageLimit;

    public InterviewAiService(OllamaService ollamaService, MessageService messageService, InterviewService interviewService, InterviewRepository interviewRepository, MessageRepository messageRepository, UserService userService, EvaluationService evaluationService) {
        this.ollamaService = ollamaService;
        this.messageService = messageService;
        this.interviewService = interviewService;
        this.interviewRepository = interviewRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.evaluationService = evaluationService;
    }

    private String buildInterviewerSystemPrompt(String jobRole, String jobDescription) {
        return "You are an expert technical interviewer conducting a professional job interview.\n\nPOSITION DETAILS:\n- Job Role: %s\n- Job Description: %s\n\nYOUR BEHAVIOR RULES:\n1. You are a strict but fair professional interviewer. Maintain a formal tone throughout.\n2. Ask ONE question at a time. Never ask multiple questions in a single message.\n3. Start the interview with a brief professional greeting, introduce yourself as the interviewer, and ask the candidate to introduce themselves.\n4. After the candidate's introduction, proceed with relevant technical and behavioral questions based on the job role and description.\n5. Ask a mix of:\n   - Technical questions specific to the job role\n   - Behavioral questions (STAR format situations)\n   - Problem-solving scenarios\n   - Questions about past experience\n6. After each answer, briefly acknowledge it (1 sentence max), then ask the next question.\n7. If the candidate asks YOU a question, answer it professionally and concisely as a real interviewer would, then continue with the next interview question.\n8. Do NOT give hints, coaching advice, or tell the candidate if their answer is correct or incorrect during the interview.\n9. Do NOT go off-topic. Stay focused on the interview.\n10. Keep your messages concise. Maximum 3-4 sentences per response.\n11. Track the flow naturally — don't repeat questions already asked.\n12. If the candidate gives a very short or unclear answer, politely ask them to elaborate.\n13. Never break character. You are the interviewer at all times.\n\nIMPORTANT: You are currently conducting a LIVE interview simulation.\nBegin professionally and keep the conversation flowing naturally.\n".formatted(jobRole, jobDescription);
    }

    private String buildOpeningPrompt(String jobRole) {
        return "The interview is starting now. Greet the candidate professionally,\nintroduce yourself as their interviewer for the %s position,\nand ask them to briefly introduce themselves.\nKeep your opening message concise and professional.\n".formatted(jobRole);
    }

    private String buildEvaluationPrompt(String jobRole, String jobDescription, String conversationTranscript) {
        return "You are an expert HR evaluator and technical assessment specialist.\n\nYou have just reviewed the following job interview transcript for the position of: %s\n\nJOB DESCRIPTION:\n%s\n\nINTERVIEW TRANSCRIPT:\n%s\n\nYOUR TASK:\nEvaluate the candidate's performance based on the interview transcript above.\nAnalyze the following criteria:\n\n1. TECHNICAL KNOWLEDGE (0-25 points): How well did the candidate demonstrate technical skills relevant to the role?\n2. COMMUNICATION SKILLS (0-20 points): Clarity, articulation, and professionalism in responses.\n3. PROBLEM SOLVING (0-20 points): Ability to think critically and solve problems.\n4. RELEVANT EXPERIENCE (0-20 points): Quality of past experience examples shared.\n5. CULTURAL FIT & ATTITUDE (0-15 points): Enthusiasm, professionalism, and attitude.\n\nSCORING RULES:\n- Be honest and objective. Do not inflate scores.\n- If the transcript is very short or incomplete, score accordingly.\n- Base your evaluation ONLY on what was actually said in the transcript.\n\nRESPOND IN THIS EXACT FORMAT (do not deviate):\n\nSCORE: [number between 0 and 100]\n\nSUMMARY: [2-3 sentences summarizing overall performance]\n\nSTRENGTHS:\n- [strength 1]\n- [strength 2]\n- [strength 3]\n\nAREAS FOR IMPROVEMENT:\n- [area 1]\n- [area 2]\n- [area 3]\n\nDETAILED FEEDBACK: [3-4 sentences of detailed constructive feedback]\n\nRECOMMENDATION: [STRONG HIRE / HIRE / CONSIDER / NO HIRE]\n\nRespond with nothing else outside this format.\n".formatted(jobRole, jobDescription, conversationTranscript);
    }

    @Transactional
    public MessageResponse generateOpeningMessage(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = this.interviewService.getInterviewEntity(interviewId, currentUser);
        long existingCount = this.messageService.countMessages(interview);
        if (existingCount > 0L) {
            log.info("Opening message already exists for interview id={}", interviewId);
            List<Message> messages = this.messageService.getMessages(interview);
            return MessageResponse.fromEntity((Message)messages.get(0));
        } else {
            String systemPrompt = this.buildInterviewerSystemPrompt(interview.getJobRole(), interview.getJobDescription());
            String openingInstruction = this.buildOpeningPrompt(interview.getJobRole());
            List<Map<String, String>> history = List.of(Map.of("role", "user", "content", openingInstruction));
            String aiResponse = this.ollamaService.chat(systemPrompt, history);
            Message saved = this.messageService.saveAiMessage(interview, aiResponse);
            log.info("Opening message generated for interview id={}", interviewId);
            return MessageResponse.fromEntity(saved);
        }
    }

    @Transactional
    public Map<String, Object> processUserMessage(Long interviewId, String userContent) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = this.interviewService.getInterviewEntity(interviewId, currentUser);
        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new InterviewAlreadyCompletedException(interviewId);
        } else {
            this.interviewService.enforceMessageLimit(interview, currentUser);
            Message userMessage = this.messageService.saveUserMessage(interview, userContent);
            List<Map<String, String>> conversationHistory = this.messageService.buildConversationHistory(interview);
            String systemPrompt = this.buildInterviewerSystemPrompt(interview.getJobRole(), interview.getJobDescription());
            String aiContent = this.ollamaService.chat(systemPrompt, conversationHistory);
            Message aiMessage = this.messageService.saveAiMessage(interview, aiContent);
            long totalMessages = this.messageService.countMessages(interview);
            boolean limitReached = !currentUser.getIsPremium() && totalMessages >= (long)this.messageLimit;
            long remaining = Math.max(0L, (long)this.messageLimit - totalMessages);
            Map<String, Object> result = new HashMap();
            result.put("userMessage", MessageResponse.fromEntity(userMessage));
            result.put("aiMessage", MessageResponse.fromEntity(aiMessage));
            result.put("messageCount", totalMessages);
            result.put("limitReached", limitReached);
            result.put("remaining", remaining);
            result.put("interviewId", interviewId);
            return result;
        }
    }

    @Transactional
    public Map<String, Object> finishInterview(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = this.interviewService.getInterviewEntity(interviewId, currentUser);
        List<Message> messages = this.messageService.getMessages(interview);
        if (messages.isEmpty()) {
            throw new RuntimeException("Cannot finish an interview with no messages.");
        } else {
            EvaluationResponse evaluation = this.evaluationService.generateEvaluation(interviewId);
            Map<String, Object> result = new HashMap();
            result.put("evaluation", evaluation);
            result.put("readinessScore", evaluation.getReadinessScore());
            result.put("readinessLabel", evaluation.getReadinessLabel());
            result.put("recommendation", evaluation.getRecommendation());
            result.put("messageCount", messages.size());
            result.put("interviewId", interviewId);
            log.info("Interview id={} finished. Score={} Label={}", new Object[]{interviewId, evaluation.getReadinessScore(), evaluation.getReadinessLabel()});
            return result;
        }
    }

    private String buildTranscript(List<Message> messages) {
        StringBuilder transcript = new StringBuilder();
        transcript.append("=== INTERVIEW TRANSCRIPT ===\n\n");

        for(Message msg : messages) {
            String speaker = msg.getSender() == SenderType.AI ? "INTERVIEWER" : "CANDIDATE";
            transcript.append("[").append(speaker).append("]: ").append(msg.getContent()).append("\n\n");
        }

        transcript.append("=== END OF TRANSCRIPT ===");
        return transcript.toString();
    }

    private double parseScoreFromEvaluation(String evaluationText) {
        try {
            Pattern pattern = Pattern.compile("SCORE:\\s*(\\d+(?:\\.\\d+)?)", 2);
            Matcher matcher = pattern.matcher(evaluationText);
            if (matcher.find()) {
                double score = Double.parseDouble(matcher.group(1));
                return Math.min((double)100.0F, Math.max((double)0.0F, score));
            } else {
                Pattern fallback = Pattern.compile("(?:score|rating|percentage)[:\\s]+(\\d+)", 2);
                Matcher fallbackMatcher = fallback.matcher(evaluationText);
                if (fallbackMatcher.find()) {
                    return Double.parseDouble(fallbackMatcher.group(1));
                } else {
                    log.warn("Could not parse score from evaluation, defaulting to 50");
                    return (double)50.0F;
                }
            }
        } catch (NumberFormatException e) {
            log.error("Error parsing score: {}", e.getMessage());
            return (double)50.0F;
        }
    }

    @Transactional(
            readOnly = true
    )
    public Map<String, Object> getInterviewStatus(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = this.interviewService.getInterviewEntity(interviewId, currentUser);
        long messageCount = this.messageService.countMessages(interview);
        boolean limitReached = !currentUser.getIsPremium() && messageCount >= (long)this.messageLimit;
        Map<String, Object> status = new HashMap();
        status.put("interviewId", interviewId);
        status.put("status", interview.getStatus().name());
        status.put("jobRole", interview.getJobRole());
        status.put("messageCount", messageCount);
        status.put("messageLimit", this.messageLimit);
        status.put("limitReached", limitReached);
        status.put("remaining", Math.max(0L, (long)this.messageLimit - messageCount));
        status.put("isPremium", currentUser.getIsPremium());
        status.put("readinessScore", interview.getReadinessScore());
        return status;
    }
}
