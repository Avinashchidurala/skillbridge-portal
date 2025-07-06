package com.skillbridge.skillbridge_portal.repository;

import com.skillbridge.skillbridge_portal.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCreatedBy(String teacherEmail);
}
