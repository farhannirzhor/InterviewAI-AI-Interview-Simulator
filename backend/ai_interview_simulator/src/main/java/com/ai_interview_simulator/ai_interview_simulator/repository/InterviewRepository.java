//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.repository;

import com.ai_interview_simulator.ai_interview_simulator.entity.Interview;
import com.ai_interview_simulator.ai_interview_simulator.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByUserOrderByCreatedAtDesc(User user);

    Optional<Interview> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    List<Interview> findByUserAndStatus(User user, Interview.InterviewStatus status);
}
