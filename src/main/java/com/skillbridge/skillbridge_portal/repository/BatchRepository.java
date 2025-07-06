package com.skillbridge.skillbridge_portal.repository;

import com.skillbridge.skillbridge_portal.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByIsClosedFalse(); // Active batches
}
