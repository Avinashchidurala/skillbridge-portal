package com.skillbridge.skillbridge_portal.service;

import com.skillbridge.skillbridge_portal.model.Batch;
import com.skillbridge.skillbridge_portal.model.User;
import com.skillbridge.skillbridge_portal.repository.BatchRepository;
import com.skillbridge.skillbridge_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private UserRepository userRepository;

    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public List<Batch> getActiveBatches() {
        return batchRepository.findByIsClosedFalse();
    }

    public Batch assignStudentsToBatch(Long batchId, List<Long> studentIds) {
        Batch batch = batchRepository.findById(batchId).orElseThrow();
        List<User> students = userRepository.findAllById(studentIds);
        batch.getStudents().addAll(students);
        return batchRepository.save(batch);
    }

    public Batch assignTeacherToBatch(Long batchId, Long teacherId) {
        Batch batch = batchRepository.findById(batchId).orElseThrow();
        User teacher = userRepository.findById(teacherId).orElseThrow();
        batch.setTeacher(teacher);
        return batchRepository.save(batch);
    }

    public Batch closeBatch(Long batchId) {
        Batch batch = batchRepository.findById(batchId).orElseThrow();
        batch.setClosed(true);
        batch.getStudents().forEach(student -> {
            student.setActive(true); // Auto-activate students
            userRepository.save(student);
        });
        return batchRepository.save(batch);
    }
}
