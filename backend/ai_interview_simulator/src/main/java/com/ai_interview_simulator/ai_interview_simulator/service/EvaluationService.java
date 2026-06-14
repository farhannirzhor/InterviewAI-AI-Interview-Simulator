//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.response.EvaluationResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.EvaluationResponse.ScoreBreakdown;
import com.ai_interview_simulator.ai_interview_simulator.entity.Interview;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import com.ai_interview_simulator.ai_interview_simulator.entity.Interview.InterviewStatus;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message.SenderType;
import com.ai_interview_simulator.ai_interview_simulator.exception.InterviewNotFoundException;
import com.ai_interview_simulator.ai_interview_simulator.repository.InterviewRepository;
import com.ai_interview_simulator.ai_interview_simulator.repository.MessageRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class EvaluationService {
    private static final Logger log = LoggerFactory.getLogger(EvaluationService.class);
    private final OllamaService ollamaService;
    private final InterviewRepository interviewRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final InterviewService interviewService;
    @Value("${interview.message.limit}")
    private int messageLimit;

    public EvaluationService(OllamaService ollamaService, InterviewRepository interviewRepository, MessageRepository messageRepository, UserService userService, InterviewService interviewService) {
        this.ollamaService = ollamaService;
        this.interviewRepository = interviewRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.interviewService = interviewService;
    }

    private String buildDetailedEvaluationPrompt(String jobRole, String jobDescription, String transcript, int totalMessages) {
        return "You are a senior HR director and technical evaluation specialist with 20+ years of experience.\n\nYou have just completed reviewing a job interview for the following position:\n\n═══════════════════════════════════════════\nPOSITION: %s\n═══════════════════════════════════════════\nJOB DESCRIPTION:\n%s\n═══════════════════════════════════════════\n\nINTERVIEW TRANSCRIPT (%d messages total):\n%s\n═══════════════════════════════════════════\n\nEVALUATION INSTRUCTIONS:\nCarefully analyze the candidate's performance across all dimensions below.\nBe honest, objective, and constructive. Do not inflate scores.\nBase your evaluation ONLY on what was actually said in the transcript.\nIf the interview was very short or incomplete, reflect that in the score.\n\nSCORING CRITERIA:\n- TECHNICAL KNOWLEDGE    : 0 to 25 points\n- COMMUNICATION SKILLS   : 0 to 20 points\n- PROBLEM SOLVING        : 0 to 20 points\n- RELEVANT EXPERIENCE    : 0 to 20 points\n- CULTURAL FIT/ATTITUDE  : 0 to 15 points\n- TOTAL                  : 0 to 100 points\n\n═══════════════════════════════════════════\nRESPOND IN EXACTLY THIS FORMAT BELOW.\nDO NOT ADD ANYTHING OUTSIDE THIS FORMAT.\nDO NOT USE MARKDOWN. DO NOT USE ASTERISKS.\n═══════════════════════════════════════════\n\nSCORE: [number 0-100]\n\nTECHNICAL_KNOWLEDGE: [number 0-25]\nCOMMUNICATION_SKILLS: [number 0-20]\nPROBLEM_SOLVING: [number 0-20]\nRELEVANT_EXPERIENCE: [number 0-20]\nCULTURAL_FIT_ATTITUDE: [number 0-15]\n\nSUMMARY: [Write 2-3 honest sentences summarizing the candidate's overall performance]\n\nSTRENGTHS:\n- [Specific strength observed in the interview]\n- [Specific strength observed in the interview]\n- [Specific strength observed in the interview]\n\nAREAS_FOR_IMPROVEMENT:\n- [Specific area that needs work based on the interview]\n- [Specific area that needs work based on the interview]\n- [Specific area that needs work based on the interview]\n\nDETAILED_FEEDBACK: [Write 3-4 sentences of specific, actionable, constructive feedback for the candidate to improve]\n\nRECOMMENDATION: [Choose exactly one: STRONG HIRE / HIRE / CONSIDER / NO HIRE]\n\n".formatted(jobRole, jobDescription, totalMessages, transcript);
    }

    private String buildTranscript(List<Message> messages) {
        StringBuilder transcript = new StringBuilder();

        for(int i = 0; i < messages.size(); ++i) {
            Message msg = (Message)messages.get(i);
            String speaker = msg.getSender() == SenderType.AI ? "INTERVIEWER" : "CANDIDATE";
            transcript.append("--- Message ").append(i + 1).append(" ---\n");
            transcript.append("[").append(speaker).append("]: ").append(msg.getContent().trim()).append("\n\n");
        }

        return transcript.toString();
    }

    private EvaluationResponse parseEvaluation(String rawText, Long interviewId, String jobRole, String jobDescription, long totalMessages) {
        EvaluationResponse.EvaluationResponseBuilder builder = EvaluationResponse.builder();
        builder.interviewId(interviewId);
        builder.jobRole(jobRole);
        builder.jobDescription(jobDescription);
        builder.totalMessages(totalMessages);
        builder.rawEvaluation(rawText);
        builder.interviewStatus("COMPLETED");
        builder.completedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        double score = this.parseDouble(rawText, "SCORE", (double)50.0F);
        score = Math.min((double)100.0F, Math.max((double)0.0F, score));
        builder.readinessScore(score);
        builder.readinessLabel(EvaluationResponse.buildReadinessLabel(score));
        EvaluationResponse.ScoreBreakdown breakdown = ScoreBreakdown.builder().technicalKnowledge(Math.min((double)25.0F, this.parseDouble(rawText, "TECHNICAL_KNOWLEDGE", (double)0.0F))).communicationSkills(Math.min((double)20.0F, this.parseDouble(rawText, "COMMUNICATION_SKILLS", (double)0.0F))).problemSolving(Math.min((double)20.0F, this.parseDouble(rawText, "PROBLEM_SOLVING", (double)0.0F))).relevantExperience(Math.min((double)20.0F, this.parseDouble(rawText, "RELEVANT_EXPERIENCE", (double)0.0F))).culturalFitAttitude(Math.min((double)15.0F, this.parseDouble(rawText, "CULTURAL_FIT_ATTITUDE", (double)0.0F))).build();
        builder.scoreBreakdown(breakdown);
        builder.summary(this.parseSection(rawText, "SUMMARY", "STRENGTHS"));
        builder.strengths(this.parseBulletList(rawText, "STRENGTHS", "AREAS_FOR_IMPROVEMENT"));
        builder.areasForImprovement(this.parseBulletList(rawText, "AREAS_FOR_IMPROVEMENT", "DETAILED_FEEDBACK"));
        builder.detailedFeedback(this.parseSection(rawText, "DETAILED_FEEDBACK", "RECOMMENDATION"));
        builder.recommendation(this.parseRecommendation(rawText));
        return builder.build();
    }

    private double parseDouble(String text, String key, double defaultValue) {
        try {
            Pattern pattern = Pattern.compile(key + ":\\s*(\\d+(?:\\.\\d+)?)", 2);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1));
            }
        } catch (NumberFormatException var7) {
            log.warn("Could not parse double for key: {}", key);
        }

        return defaultValue;
    }

    private String parseSection(String text, String startKey, String endKey) {
        try {
            Pattern pattern = Pattern.compile(startKey + ":\\s*(.+?)(?=" + endKey + ":|$)", 34);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return matcher.group(1).replaceAll("\\n+", " ").trim();
            }
        } catch (Exception var6) {
            log.warn("Could not parse section: {}", startKey);
        }

        return "Not available";
    }

    private List<String> parseBulletList(String text, String startKey, String endKey) {
        List<String> items = new ArrayList();

        try {
            Pattern sectionPattern = Pattern.compile(startKey + ":\\s*(.+?)(?=" + endKey + ":|$)", 34);
            Matcher sectionMatcher = sectionPattern.matcher(text);
            if (sectionMatcher.find()) {
                String section = sectionMatcher.group(1);
                Pattern bulletPattern = Pattern.compile("^\\s*-\\s*(.+)$", 8);
                Matcher bulletMatcher = bulletPattern.matcher(section);

                while(bulletMatcher.find()) {
                    String item = bulletMatcher.group(1).trim();
                    if (!item.isEmpty()) {
                        items.add(item);
                    }
                }
            }
        } catch (Exception var11) {
            log.warn("Could not parse bullet list for key: {}", startKey);
        }

        if (items.isEmpty()) {
            items.add("Not available");
        }

        return items;
    }

    private String parseRecommendation(String text) {
        String[] recommendations = new String[]{"STRONG HIRE", "HIRE", "CONSIDER", "NO HIRE"};
        String upperText = text.toUpperCase();

        for(String rec : recommendations) {
            if (upperText.contains("RECOMMENDATION: " + rec) || upperText.contains("RECOMMENDATION:" + rec)) {
                return rec;
            }
        }

        for(String rec : recommendations) {
            if (upperText.contains(rec)) {
                return rec;
            }
        }

        return "CONSIDER";
    }

    @Transactional
    public EvaluationResponse generateEvaluation(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        List<Message> messages = this.messageRepository.findByInterviewOrderByTimestampAsc(interview);
        if (messages.isEmpty()) {
            throw new RuntimeException("Cannot evaluate an interview with no messages. Please have at least one conversation exchange.");
        } else {
            log.info("Generating evaluation for interview id={} with {} messages", interviewId, messages.size());
            String transcript = this.buildTranscript(messages);
            String prompt = this.buildDetailedEvaluationPrompt(interview.getJobRole(), interview.getJobDescription(), transcript, messages.size());
            log.info("Calling Ollama for evaluation...");
            String rawEvaluation = this.ollamaService.generate(prompt);
            log.info("Raw evaluation received, length={}", rawEvaluation.length());
            EvaluationResponse evaluation = this.parseEvaluation(rawEvaluation, interviewId, interview.getJobRole(), interview.getJobDescription(), (long)messages.size());
            interview.setReadinessScore(evaluation.getReadinessScore());
            interview.setFeedback(rawEvaluation);
            interview.setStatus(InterviewStatus.COMPLETED);
            this.interviewRepository.save(interview);
            log.info("Evaluation saved — interview id={} score={}", interviewId, evaluation.getReadinessScore());
            return evaluation;
        }
    }

    @Transactional(
            readOnly = true
    )
    public EvaluationResponse getSavedEvaluation(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        if (interview.getStatus() != InterviewStatus.COMPLETED) {
            throw new RuntimeException("This interview has not been completed yet. Please finish the interview first.");
        } else {
            long totalMessages = this.messageRepository.countByInterview(interview);
            return interview.getFeedback() != null && !interview.getFeedback().isEmpty() ? this.parseEvaluation(interview.getFeedback(), interviewId, interview.getJobRole(), interview.getJobDescription(), totalMessages) : EvaluationResponse.builder().interviewId(interviewId).jobRole(interview.getJobRole()).jobDescription(interview.getJobDescription()).readinessScore(interview.getReadinessScore()).readinessLabel(EvaluationResponse.buildReadinessLabel(interview.getReadinessScore())).totalMessages(totalMessages).interviewStatus(interview.getStatus().name()).summary("Evaluation details not available.").strengths(List.of("Not available")).areasForImprovement(List.of("Not available")).detailedFeedback("Please re-run the evaluation.").recommendation("CONSIDER").completedAt(interview.getUpdatedAt() != null ? interview.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "Unknown").build();
        }
    }

    @Transactional(
            readOnly = true
    )
    public Map<String, Object> getScoreSummary(Long interviewId) {
        User currentUser = this.userService.getCurrentUser();
        Interview interview = (Interview)this.interviewRepository.findByIdAndUser(interviewId, currentUser).orElseThrow(() -> new InterviewNotFoundException(interviewId));
        long totalMessages = this.messageRepository.countByInterview(interview);
        Map<String, Object> summary = new HashMap();
        summary.put("interviewId", interviewId);
        summary.put("jobRole", interview.getJobRole());
        summary.put("status", interview.getStatus().name());
        summary.put("readinessScore", interview.getReadinessScore());
        summary.put("readinessLabel", EvaluationResponse.buildReadinessLabel(interview.getReadinessScore()));
        summary.put("totalMessages", totalMessages);
        summary.put("isCompleted", interview.getStatus() == InterviewStatus.COMPLETED);
        summary.put("completedAt", interview.getUpdatedAt() != null ? interview.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
        return summary;
    }
}
