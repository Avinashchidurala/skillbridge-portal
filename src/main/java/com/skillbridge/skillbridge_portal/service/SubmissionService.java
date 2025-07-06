package com.skillbridge.skillbridge_portal.service;

import com.skillbridge.skillbridge_portal.model.Exam;
import com.skillbridge.skillbridge_portal.model.Question;
import com.skillbridge.skillbridge_portal.model.Submission;
import com.skillbridge.skillbridge_portal.repository.ExamRepository;
import com.skillbridge.skillbridge_portal.repository.SubmissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ExamRepository examRepository;

    public Submission submitAnswers(Long examId, Long studentId, Map<Long, String> answers) {
        Exam exam = examRepository.findById(examId).orElseThrow();

        int total = exam.getQuestions().size();
        int correct = 0;

        for (Question q : exam.getQuestions()) {
            String submitted = answers.get(q.getId());
            if (submitted != null && submitted.equalsIgnoreCase(q.getCorrectOption())) {
                correct++;
            }
        }

        Submission submission = new Submission();
        submission.setExamId(examId);
        submission.setStudentId(studentId);
        submission.setAnswers(answers);
        submission.setTotalQuestions(total);
        submission.setCorrectAnswers(correct);
        submission.setScore((int) ((correct * 100.0) / total));
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByStudent(Long studentId) {
        return submissionRepository.findByStudentId(studentId);
    }

    public List<Submission> getSubmissionsByExam(Long examId) {
        return submissionRepository.findByExamId(examId);
    }
}
