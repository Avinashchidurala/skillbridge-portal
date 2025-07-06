package com.skillbridge.skillbridge_portal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String subject;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String createdBy; // email or uniqueId of teacher

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Question> questions;
}
