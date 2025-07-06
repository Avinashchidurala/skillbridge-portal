package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.Exam;
import com.skillbridge.skillbridge_portal.model.Question;
import com.skillbridge.skillbridge_portal.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin
public class ExamController {

    @Autowired
    private ExamService examService;

    // ✅ Create Exam (Teacher Only)
    @PostMapping("/create")
    public ResponseEntity<?> createExam(@RequestBody ExamRequest request) {
        Exam savedExam = examService.createExamWithQuestions(request.getExam(), request.getQuestions());
        return ResponseEntity.ok(savedExam);
    }

    // 🧑‍🏫 Get Teacher's Exams
    @GetMapping("/teacher/{email}")
    public List<Exam> getExamsByTeacher(@PathVariable String email) {
        return examService.getExamsByTeacher(email);
    }

    // 🎓 Get All Exams (Student View)
    @GetMapping("/all")
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    // 📄 Get Single Exam Details
    @GetMapping("/{id}")
    public Exam getExamById(@PathVariable Long id) {
        return examService.getExamById(id);
    }

    // ⚙️ DTO for Request Payload
    public static class ExamRequest {
        private Exam exam;
        private List<Question> questions;

        public Exam getExam() { return exam; }
        public void setExam(Exam exam) { this.exam = exam; }

        public List<Question> getQuestions() { return questions; }
        public void setQuestions(List<Question> questions) { this.questions = questions; }
    }
}
