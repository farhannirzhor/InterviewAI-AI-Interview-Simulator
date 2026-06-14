//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.response;

import com.ai_interview_simulator.ai_interview_simulator.entity.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentResponse {
    private Long paymentId;
    private Long userId;
    private String userName;
    private String userEmail;
    private String plan;
    private BigDecimal amount;
    private String status;
    private String transactionId;
    private Boolean isPremium;
    private String premiumMessage;
    private String createdAt;
    private String updatedAt;
    private String demoNotice;

    public PaymentResponse() {
    }

    public PaymentResponse(Long paymentId, Long userId, String userName, String userEmail, String plan, BigDecimal amount, String status, String transactionId, Boolean isPremium, String premiumMessage, String createdAt, String updatedAt, String demoNotice) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.plan = plan;
        this.amount = amount;
        this.status = status;
        this.transactionId = transactionId;
        this.isPremium = isPremium;
        this.premiumMessage = premiumMessage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.demoNotice = demoNotice;
    }

    public Long getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPlan() {
        return this.plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Boolean getIsPremium() {
        return this.isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public String getPremiumMessage() {
        return this.premiumMessage;
    }

    public void setPremiumMessage(String premiumMessage) {
        this.premiumMessage = premiumMessage;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDemoNotice() {
        return this.demoNotice;
    }

    public void setDemoNotice(String demoNotice) {
        this.demoNotice = demoNotice;
    }

    public static PaymentResponseBuilder builder() {
        return new PaymentResponseBuilder();
    }

    public static PaymentResponse fromEntity(Payment payment, String userName, String userEmail, String plan, boolean isPremium) {
        return builder().paymentId(payment.getId()).userId(payment.getUser().getId()).userName(userName).userEmail(userEmail).plan(plan).amount(payment.getAmount()).status(payment.getStatus().name()).transactionId(generateTransactionId(payment.getId())).isPremium(isPremium).premiumMessage(isPremium ? "Your account has been upgraded to Premium. Enjoy unlimited interview sessions!" : "Payment pending. Complete payment to unlock premium.").createdAt(formatDateTime(payment.getCreatedAt())).updatedAt(formatDateTime(payment.getUpdatedAt())).demoNotice("This is a demo payment system. No real transaction has been processed.").build();
    }

    private static String generateTransactionId(Long paymentId) {
        long var10000 = System.currentTimeMillis();
        return "TXN-" + var10000 + "-" + String.format("%04d", paymentId);
    }

    private static String formatDateTime(LocalDateTime dt) {
        return dt == null ? "N/A" : dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static class PaymentResponseBuilder {
        private Long paymentId;
        private Long userId;
        private String userName;
        private String userEmail;
        private String plan;
        private BigDecimal amount;
        private String status;
        private String transactionId;
        private Boolean isPremium;
        private String premiumMessage;
        private String createdAt;
        private String updatedAt;
        private String demoNotice;

        PaymentResponseBuilder() {
        }

        public PaymentResponseBuilder paymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public PaymentResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public PaymentResponseBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public PaymentResponseBuilder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public PaymentResponseBuilder plan(String plan) {
            this.plan = plan;
            return this;
        }

        public PaymentResponseBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public PaymentResponseBuilder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public PaymentResponseBuilder isPremium(Boolean isPremium) {
            this.isPremium = isPremium;
            return this;
        }

        public PaymentResponseBuilder premiumMessage(String premiumMessage) {
            this.premiumMessage = premiumMessage;
            return this;
        }

        public PaymentResponseBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PaymentResponseBuilder updatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PaymentResponseBuilder demoNotice(String demoNotice) {
            this.demoNotice = demoNotice;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(this.paymentId, this.userId, this.userName, this.userEmail, this.plan, this.amount, this.status, this.transactionId, this.isPremium, this.premiumMessage, this.createdAt, this.updatedAt, this.demoNotice);
        }
    }
}
