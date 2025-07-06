package com.skillbridge.skillbridge_portal.service;

import com.skillbridge.skillbridge_portal.model.Exam;
import com.skillbridge.skillbridge_portal.model.Question;
import com.skillbridge.skillbridge_portal.repository.ExamRepository;
import com.skillbridge.skillbridge_portal.repository.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Exam createExamWithQuestions(Exam exam, List<Question> questions) {
        Exam savedExam = examRepository.save(exam);
        questions.forEach(q -> q.setExam(savedExam));
        questionRepository.saveAll(questions);
        return savedExam;
    }

    public List<Exam> getExamsByTeacher(String teacherEmail) {
        return examRepository.findByCreatedBy(teacherEmail);
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExamById(Long id) {
        return examRepository.findById(id).orElse(null);
    }
}
