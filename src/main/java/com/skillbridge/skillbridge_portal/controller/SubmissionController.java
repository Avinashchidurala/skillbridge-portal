package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.Submission;
import com.skillbridge.skillbridge_portal.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    // 📝 Submit Exam Answers
    @PostMapping("/submit")
    public ResponseEntity<Submission> submitExam(
            @RequestParam Long examId,
            @RequestParam Long studentId,
            @RequestBody Map<Long, String> answers // questionId → selectedOption
    ) {
        Submission submission = submissionService.submitAnswers(examId, studentId, answers);
        return ResponseEntity.ok(submission);
    }

    // 👤 Get All Submissions by Student
    @GetMapping("/student/{studentId}")
    public List<Submission> getSubmissionsByStudent(@PathVariable Long studentId) {
        return submissionService.getSubmissionsByStudent(studentId);
    }

    // 📊 Get All Submissions for an Exam (Teacher View)
    @GetMapping("/exam/{examId}")
    public List<Submission> getSubmissionsByExam(@PathVariable Long examId) {
        return submissionService.getSubmissionsByExam(examId);
    }
}
