package com.skillbridge.skillbridge_portal.controller;

import com.skillbridge.skillbridge_portal.model.Batch;
import com.skillbridge.skillbridge_portal.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@CrossOrigin
public class BatchController {

    @Autowired
    private BatchService batchService;

    // 🆕 Create a New Batch
    @PostMapping("/create")
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.createBatch(batch));
    }

    // 🧑‍🏫 Assign Teacher to Batch
    @PutMapping("/assign-teacher")
    public ResponseEntity<Batch> assignTeacher(
            @RequestParam Long batchId,
            @RequestParam Long teacherId
    ) {
        return ResponseEntity.ok(batchService.assignTeacherToBatch(batchId, teacherId));
    }

    // 👨‍🎓 Assign Students to Batch
    @PutMapping("/assign-students")
    public ResponseEntity<Batch> assignStudents(
            @RequestParam Long batchId,
            @RequestBody List<Long> studentIds
    ) {
        return ResponseEntity.ok(batchService.assignStudentsToBatch(batchId, studentIds));
    }

    // 🚪 Close Batch and Activate Students
    @PutMapping("/close/{batchId}")
    public ResponseEntity<Batch> closeBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(batchService.closeBatch(batchId));
    }

    // 📋 Get All Active Batches
    @GetMapping("/active")
    public ResponseEntity<List<Batch>> getActiveBatches() {
        return ResponseEntity.ok(batchService.getActiveBatches());
    }
}
