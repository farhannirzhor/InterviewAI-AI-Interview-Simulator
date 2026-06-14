//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.controller;

import com.ai_interview_simulator.ai_interview_simulator.dto.request.PaymentRequest;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.ApiResponse;
import com.ai_interview_simulator.ai_interview_simulator.dto.response.PaymentResponse;
import com.ai_interview_simulator.ai_interview_simulator.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/payments"})
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping({"/plans"})
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPlans() {
        List<Map<String, Object>> plans = this.paymentService.getAvailablePlans();
        return ResponseEntity.ok(ApiResponse.success("Plans retrieved", plans));
    }

    @GetMapping({"/premium-status"})
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPremiumStatus() {
        Map<String, Object> status = this.paymentService.getPremiumStatus();
        return ResponseEntity.ok(ApiResponse.success("Premium status retrieved", status));
    }

    @PostMapping({"/initiate"})
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(@RequestBody @Valid PaymentRequest request) {
        log.info("Payment initiation requested for plan: {}", request.getPlan());
        PaymentResponse response = this.paymentService.initiatePayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment initiated successfully. Proceed to confirm to activate premium.", response));
    }

    @PostMapping({"/confirm/{paymentId}"})
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmPayment(@PathVariable Long paymentId) {
        log.info("Payment confirmation requested for paymentId: {}", paymentId);
        PaymentResponse response = this.paymentService.confirmPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Payment confirmed! Your account has been upgraded to Premium.", response));
    }

    @PostMapping({"/cancel/{paymentId}"})
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(@PathVariable Long paymentId) {
        log.info("Payment cancellation requested for paymentId: {}", paymentId);
        PaymentResponse response = this.paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Payment cancelled successfully.", response));
    }

    @GetMapping({"/status/{paymentId}"})
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentStatus(@PathVariable Long paymentId) {
        PaymentResponse response = this.paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Payment status retrieved", response));
    }

    @GetMapping({"/my"})
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPayments() {
        List<PaymentResponse> payments = this.paymentService.getMyPayments();
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved", payments));
    }
}
