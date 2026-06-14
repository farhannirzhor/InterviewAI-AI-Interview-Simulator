//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ai_interview_simulator.ai_interview_simulator.repository;

import com.ai_interview_simulator.ai_interview_simulator.entity.Interview;
import com.ai_interview_simulator.ai_interview_simulator.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByInterviewOrderByTimestampAsc(Interview interview);

    long countByInterview(Interview interview);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.interview = :interview AND m.sender = 'USER'")
    long countUserMessagesByInterview(@Param("interview") Interview interview);

    void deleteByInterview(Interview interview);
}
