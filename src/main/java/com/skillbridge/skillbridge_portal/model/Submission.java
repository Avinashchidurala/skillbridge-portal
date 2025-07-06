
package com.skillbridge.skillbridge_portal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long examId;
    private Long studentId;

    private int totalQuestions;
    private int correctAnswers;
    private int score;

    private LocalDateTime submittedAt;

    @ElementCollection
    @CollectionTable(name = "submitted_answers", joinColumns = @JoinColumn(name = "submission_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "selected_option")
    private Map<Long, String> answers; // questionId → selectedOption
}
