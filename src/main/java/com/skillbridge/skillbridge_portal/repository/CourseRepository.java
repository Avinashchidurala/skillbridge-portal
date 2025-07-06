package com.skillbridge.skillbridge_portal.repository;

import com.skillbridge.skillbridge_portal.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
