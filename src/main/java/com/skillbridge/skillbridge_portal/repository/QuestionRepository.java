package com.skillbridge.skillbridge_portal.repository;

import com.skillbridge.skillbridge_portal.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
