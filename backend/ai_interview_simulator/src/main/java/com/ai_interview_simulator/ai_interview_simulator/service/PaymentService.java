//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.service;

import com.ai_interview_simulator.ai_interview_simulator.dto.request.PaymentRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.PaymentResponse;
import com.ai_interview_simulator.ai_interview_simulator.entity.Payment;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import com.ai_interview_simulator.ai_interview_simulator.entity.Payment.PaymentStatus;
import com.ai_interview_simulator.ai_interview_simulator.exception.PaymentException;
import com.ai_interview_simulator.ai_interview_simulator.repository.PaymentRepository;
import com.ai_interview_simulator.ai_interview_simulator.repository.UserRepository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private static final Map<String, BigDecimal> PLANS = Map.of("BASIC", new BigDecimal("9.99"), "PRO", new BigDecimal("19.99"), "UNLIMITED", new BigDecimal("49.99"));
    private static final Map<String, String> PLAN_DESCRIPTIONS = Map.of("BASIC", "50 interview sessions per month — Perfect for beginners", "PRO", "200 interview sessions per month — Great for active job seekers", "UNLIMITED", "Unlimited interview sessions — For power users and professionals");

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        User currentUser = this.userService.getCurrentUser();
        String plan = request.getPlan().toUpperCase();
        if (!PLANS.containsKey(plan)) {
            throw new PaymentException("Invalid payment plan: " + request.getPlan() + ". Valid plans are: BASIC, PRO, UNLIMITED");
        } else {
            boolean alreadyPremium = this.paymentRepository.existsByUserAndStatus(currentUser, PaymentStatus.SUCCESS);
            if (alreadyPremium) {
                throw new PaymentException("You already have an active premium subscription. No further payment is needed.");
            } else {
                BigDecimal amount = (BigDecimal)PLANS.get(plan);
                Payment payment = Payment.builder().user(currentUser).amount(amount).status(PaymentStatus.PENDING).build();
                Payment saved = (Payment)this.paymentRepository.save(payment);
                log.info("Payment initiated — user={} plan={} amount={}", new Object[]{currentUser.getEmail(), plan, amount});
                return PaymentResponse.fromEntity(saved, currentUser.getName(), currentUser.getEmail(), plan, false);
            }
        }
    }

    @Transactional
    public PaymentResponse confirmPayment(Long paymentId) {
        User currentUser = this.userService.getCurrentUser();
        Payment payment = (Payment)this.paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentException("Payment not found with id: " + String.valueOf(paymentId)));
        if (!payment.getUser().getId().equals(currentUser.getId())) {
            throw new PaymentException("You are not authorized to confirm this payment.");
        } else if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentException("This payment has already been processed. Status: " + payment.getStatus().name());
        } else {
            payment.setStatus(PaymentStatus.SUCCESS);
            Payment confirmed = (Payment)this.paymentRepository.save(payment);
            currentUser.setIsPremium(true);
            this.userRepository.save(currentUser);
            log.info("Payment confirmed — user={} paymentId={} upgraded to PREMIUM", currentUser.getEmail(), paymentId);
            String plan = this.resolvePlanFromAmount(payment.getAmount());
            return PaymentResponse.fromEntity(confirmed, currentUser.getName(), currentUser.getEmail(), plan, true);
        }
    }

    @Transactional
    public PaymentResponse cancelPayment(Long paymentId) {
        User currentUser = this.userService.getCurrentUser();
        Payment payment = (Payment)this.paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentException("Payment not found with id: " + String.valueOf(paymentId)));
        if (!payment.getUser().getId().equals(currentUser.getId())) {
            throw new PaymentException("You are not authorized to cancel this payment.");
        } else if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentException("Only PENDING payments can be cancelled. Current status: " + payment.getStatus().name());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            Payment cancelled = (Payment)this.paymentRepository.save(payment);
            log.info("Payment cancelled — user={} paymentId={}", currentUser.getEmail(), paymentId);
            String plan = this.resolvePlanFromAmount(payment.getAmount());
            return PaymentResponse.fromEntity(cancelled, currentUser.getName(), currentUser.getEmail(), plan, false);
        }
    }

    @Transactional(
            readOnly = true
    )
    public PaymentResponse getPaymentStatus(Long paymentId) {
        User currentUser = this.userService.getCurrentUser();
        Payment payment = (Payment)this.paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentException("Payment not found with id: " + String.valueOf(paymentId)));
        if (!payment.getUser().getId().equals(currentUser.getId())) {
            throw new PaymentException("You are not authorized to view this payment.");
        } else {
            String plan = this.resolvePlanFromAmount(payment.getAmount());
            boolean isPremium = currentUser.getIsPremium();
            return PaymentResponse.fromEntity(payment, currentUser.getName(), currentUser.getEmail(), plan, isPremium);
        }
    }

    @Transactional(
            readOnly = true
    )
    public List<PaymentResponse> getMyPayments() {
        User currentUser = this.userService.getCurrentUser();
        return (List)this.paymentRepository.findByUserOrderByCreatedAtDesc(currentUser).stream().map((payment) -> PaymentResponse.fromEntity(payment, currentUser.getName(), currentUser.getEmail(), this.resolvePlanFromAmount(payment.getAmount()), currentUser.getIsPremium())).collect(Collectors.toList());
    }

    @Transactional(
            readOnly = true
    )
    public Map<String, Object> getPremiumStatus() {
        User currentUser = this.userService.getCurrentUser();
        boolean isPremium = currentUser.getIsPremium();
        Payment latestPayment = (Payment)this.paymentRepository.findTopByUserAndStatusOrderByCreatedAtDesc(currentUser, PaymentStatus.SUCCESS).orElse((Object)null);
        Map<String, Object> status = new HashMap();
        status.put("userId", currentUser.getId());
        status.put("email", currentUser.getEmail());
        status.put("isPremium", isPremium);
        status.put("premiumStatus", isPremium ? "ACTIVE" : "INACTIVE");
        status.put("messageLimit", isPremium ? "Unlimited" : "20 messages per interview");
        if (latestPayment != null) {
            status.put("lastPaymentId", latestPayment.getId());
            status.put("lastPaymentAmount", latestPayment.getAmount());
            status.put("lastPaymentPlan", this.resolvePlanFromAmount(latestPayment.getAmount()));
            status.put("lastPaymentDate", latestPayment.getUpdatedAt() != null ? latestPayment.getUpdatedAt().toString() : latestPayment.getCreatedAt().toString());
        }

        status.put("upgradeMessage", isPremium ? "You have full premium access. Enjoy unlimited interviews!" : "Upgrade to Premium to remove the 20 message limit and get unlimited interview practice.");
        return status;
    }

    public List<Map<String, Object>> getAvailablePlans() {
        return (List)PLANS.entrySet().stream().map((entry) -> {
            Map<String, Object> plan = new HashMap();
            plan.put("planId", entry.getKey());
            plan.put("name", this.formatPlanName((String)entry.getKey()));
            plan.put("price", entry.getValue());
            plan.put("currency", "USD");
            plan.put("description", PLAN_DESCRIPTIONS.get(entry.getKey()));
            plan.put("features", this.getPlanFeatures((String)entry.getKey()));
            plan.put("recommended", ((String)entry.getKey()).equals("PRO"));
            return plan;
        }).collect(Collectors.toList());
    }

    private String resolvePlanFromAmount(BigDecimal amount) {
        return (String)PLANS.entrySet().stream().filter((entry) -> ((BigDecimal)entry.getValue()).compareTo(amount) == 0).map(Map.Entry::getKey).findFirst().orElse("CUSTOM");
    }

    private String formatPlanName(String planKey) {
        String var10000;
        switch (planKey) {
            case "PRO":
                var10000 = "Pro Plan";
                return var10000;
            case "BASIC":
                var10000 = "Basic Plan";
                return var10000;
            case "UNLIMITED":
                var10000 = "Unlimited Plan";
                return var10000;
        }

        var10000 = planKey;
        return var10000;
    }

    private List<String> getPlanFeatures(String planKey) {
        List var10000;
        switch (planKey) {
            case "PRO":
                var10000 = List.of("200 interview sessions per month", "AI-powered detailed feedback", "Full readiness score breakdown", "Advanced question bank", "Performance history tracking", "Priority AI response speed");
                return var10000;
            case "BASIC":
                var10000 = List.of("50 interview sessions per month", "AI-powered feedback", "Readiness score report", "Basic question bank");
                return var10000;
            case "UNLIMITED":
                var10000 = List.of("Unlimited interview sessions", "AI-powered detailed feedback", "Full readiness score breakdown", "Complete question bank", "Full performance history", "Fastest AI response speed", "Export interview reports", "Premium support");
                return var10000;
        }

        var10000 = List.of("Standard features");
        return var10000;
    }
}
