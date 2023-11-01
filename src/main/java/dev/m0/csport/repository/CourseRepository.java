package dev.m0.csport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.m0.csport.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    
    @Query("SELECT c FROM Course c WHERE c.places > 0 AND c.coach IS NOT NULL")
    List<Course> findAvailableCourses();
}

