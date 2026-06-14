//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.repository;

import com.ai_interview_simulator.ai_interview_simulator.entity.Payment;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserOrderByCreatedAtDesc(User user);

    Optional<Payment> findTopByUserAndStatusOrderByCreatedAtDesc(User user, Payment.PaymentStatus status);

    boolean existsByUserAndStatus(User user, Payment.PaymentStatus status);
}
