//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class PaymentRequest {
    private @NotBlank(
            message = "Payment plan is required"
    ) String plan;
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private BigDecimal amount;

    public PaymentRequest() {
    }

    public PaymentRequest(String plan, String cardHolderName, String cardNumber, String expiryDate, String cvv, BigDecimal amount) {
        this.plan = plan;
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.amount = amount;
    }

    public String getPlan() {
        return this.plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return this.cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static PaymentRequestBuilder builder() {
        return new PaymentRequestBuilder();
    }

    public static class PaymentRequestBuilder {
        private String plan;
        private String cardHolderName;
        private String cardNumber;
        private String expiryDate;
        private String cvv;
        private BigDecimal amount;

        PaymentRequestBuilder() {
        }

        public PaymentRequestBuilder plan(String plan) {
            this.plan = plan;
            return this;
        }

        public PaymentRequestBuilder cardHolderName(String cardHolderName) {
            this.cardHolderName = cardHolderName;
            return this;
        }

        public PaymentRequestBuilder cardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public PaymentRequestBuilder expiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public PaymentRequestBuilder cvv(String cvv) {
            this.cvv = cvv;
            return this;
        }

        public PaymentRequestBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(this.plan, this.cardHolderName, this.cardNumber, this.expiryDate, this.cvv, this.amount);
        }
    }
}
