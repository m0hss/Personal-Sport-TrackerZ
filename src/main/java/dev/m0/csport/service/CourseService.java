package dev.m0.csport.service;

import dev.m0.csport.model.Course;
import dev.m0.csport.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.*;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository; // Vous devrez créer ce repository.

    static List<Course> courses = new ArrayList<Course>();
   

    public List<Course> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        return allCourses;
    }

    public Optional<Course> getByID(Integer courseId) {
        return courseRepository.findById(courseId);
    }

    public List<Course> getAvailableCourses() {
        // Vous pouvez utiliser le repository pour interroger la base de données.
        List<Course> allCourses = courseRepository.findAll();

        // Filtrez les cours disponibles en fonction de vos règles de gestion.
        List<Course> availableCourses = allCourses.stream()
                .filter(course -> course.getPlaces() > 0 && course.isAvailable())
                .collect(Collectors.toList());

        return availableCourses;
    }

    public Course saveCourse(Course course){
        return courseRepository.save(course);
    }

    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
      }
}