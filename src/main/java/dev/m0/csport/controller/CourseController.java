package dev.m0.csport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.m0.csport.model.Course;
import dev.m0.csport.model.URole;
import dev.m0.csport.model.User;
import dev.m0.csport.service.CourseService;
import dev.m0.csport.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// import java.net.URI;
import java.util.*;

@Tag(name = "2. COURSE", description = "Courses Management APIs")
@RestController
@RequestMapping("/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService; 

    @GetMapping
    @Operation(
        summary = "ALL Courses")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Course.class), mediaType = "application/json") })})
    public ResponseEntity<List<Course>> listCourses() {
        // Utilisez le service pour récupérer la liste des cours disponibles
        List<Course> courses = new ArrayList<Course>();
        courses = courseService.getAllCourses();
			if (courses.isEmpty()) 
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			return new ResponseEntity<>(courses, HttpStatus.OK);     
    }

    @Operation(
        summary = "Get Course By Id")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Course.class), mediaType = "application/json") })})
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getUser(@PathVariable Integer courseId) {
        Optional<Course> course = courseService.getByID(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }



// ########################## Lister les cours disponibles ###############################################
//  ○ GET /v1/courses
//  ○ Règles de gestions :
//  - Un cours disponible est un cours dont la quantité de places restantes est supérieure à 0 et pour lequel un COACH est attaché
    @GetMapping("/available")
    @Operation(
        summary = "All Available Course",
        description = "Available courses are those with a Registred Coach ")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Course.class), mediaType = "application/json") })})
    public List<Course> listAvailableCourses() {
        // Utilisez le service pour récupérer la liste des cours disponibles
        return courseService.getAvailableCourses();
    }

    @PostMapping
    @Operation(
        summary = "Create New Course",
        description = "The course is checked if it exists or not with its name.")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Course.class), mediaType = "application/json") })})
	public ResponseEntity<Object> createCourse(@RequestBody Course course, UriComponentsBuilder builder) {
      
        List<Course> courses = courseService.getAllCourses();
      
        if (courses.stream().anyMatch(cr -> cr.getName().equals(course.getName()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course Already Exists !!");
        }
        else {

		    courseService.saveCourse(course);
            // URI location =  builder.path("/v1/courses/{id}").buildAndExpand(course.getId()).toUri();
		    return new ResponseEntity<>(course, HttpStatus.OK); 
	    }
    }

    @DeleteMapping
    @Operation(
        summary = "Delete Course")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") })})
	public ResponseEntity<Object> deleteCourse(@RequestParam Integer courseId) {
      
        if (courseService.getByID(courseId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course Not Found !! !!");
        }
        else {
		    courseService.deleteCourse(courseId);
            return ResponseEntity.status(HttpStatus.OK).body("Course Deleted.");
	    }
    }




//   ############################## S’inscrire à un cours ##########################################################
//  ○ PUT /v1/courses/{course_id}/register
//  ○ Règles de gestions :
//  - Un cours peut être rejoint avec un utilisateur de type USER ou de type COACH
// - Dès qu’un utilisateur de type COACH s’inscrit, le cours devient disponible et accessible pour un utilisateur USER.
// - Chaque cours possède un nombre de places limitées. Le cours ne doit pas être rempli afin que l’utilisateur USER puisse s’inscrire.
     @Operation(
        summary = "Subscribe For a Course",
        description = "Simple Users can only register after a coach registration, Course will be Available for users and coach will be assigned to entity coach attribute ")
    @PutMapping("/{courseId}/register")
    
    public ResponseEntity<Object> register( @PathVariable Integer courseId, @RequestParam Integer userId) {
        
       
        Optional<User> optionalUser = userService.getByID(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userId not found !!");
        }
        
        User user = optionalUser.get();
        Optional<Course> optionalCourse = courseService.getByID(courseId);
        Course course = optionalCourse.get();

         // Vérifiez si le cours est complet
        if (course.isFull()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course is Full !!");
         }
       
        // Ajoutez l'utilisateur(User && Coach) au cours
        if (course != null && user != null){
            if (!course.getUsers().stream().anyMatch(us -> us.getId().equals(user.getId()))) {    
                if (course.isAvailable()){
                    if (URole.USER.equals(user.getRole())){
                        
                        course.updatePlaces();
                        user.addCourse(course);
                        courseService.saveCourse(course);
                        return ResponseEntity.ok("User registered for the course.");
                    } 
                    else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A Coach is already set to this Course !");
                    }
                }
                else {
                    if (URole.COACH.equals(user.getRole())) {
                        course.setAvailable(true);
                        course.setCoach(user);
                        // user.addCourse(course);
                        courseService.saveCourse(course);
                        return ResponseEntity.ok("HI Coach you are registred :)");
                    }
                    else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course Not Available !");
                    }
                }
                 
            }    
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already registered for this course.");
                }
            }
        else 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course or User not found.");
        }
    }



}