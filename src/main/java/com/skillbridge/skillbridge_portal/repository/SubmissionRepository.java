package com.skillbridge.skillbridge_portal.repository;

import com.skillbridge.skillbridge_portal.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentId(Long studentId);
    List<Submission> findByExamId(Long examId);
}
