package dev.m0.csport.controller;


import dev.m0.csport.service.CourseService;
import dev.m0.csport.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import dev.m0.csport.model.Course;
import dev.m0.csport.model.URole;
import dev.m0.csport.model.User;

// import java.net.URI;
import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.server.ResponseStatusException;
// import org.springframework.web.util.UriComponentsBuilder;



@Tag(name = "1. USER", description = "Users Management APIs")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService; 
    @Autowired
    private CourseService courseService;


    @Operation(
        summary = "Get All Users")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") })})
    @GetMapping
	public ResponseEntity<List<User>> geAllusers() {
		try {
			List<User> users = new ArrayList<User>();
			users = userService.getAllUsers();
			if (users.isEmpty()) 
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			return new ResponseEntity<>(users, HttpStatus.OK);
		}
        catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @Operation(
        summary = "Retrieve User By Id")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") })})
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Integer userId) {
        Optional<User> user =userService.getByID(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

//   ################ Enregistrement de l’utilisateur #################################
//  ○ POST /v1/users
//   ○ Règles de gestions :
//  ■ L’utilisateur peut être soit de type COACH ou de type USER
//  ■ Le COACH doit être majeur
//  ■ Si succès retourner l’ID de l’utilisateur créé
//  ■ Si erreur, retourner un message d’erreur avec le code HTTP adéquat
    @Operation(
        summary = "Add New User")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") })})
    @PostMapping
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        
        List<User> users = userService.getAllUsers();
        // ## Check if user COACH is Adult or Not
        if ((URole.COACH.equals(user.getRole()) && (user.isUnderage()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coach Must be Adult !!");
        }
        // Check By Email if user Exist or not 
        else if (users.stream().anyMatch(us -> us.getEmail().equals(user.getEmail()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exists !!");
        }
        
        else {
            userService.saveUser(user);
            // URI location =  builder.path("/v1/users/{id}").buildAndExpand(user.getId()).toUri();
            return new ResponseEntity<>(user, HttpStatus.OK); 
        }
    }


    // ################ Voir mes cours ################
    // ○ GET /v1/users/{user_id}/courses
    // ○ Règles de gestions :
    // - Retourner les sessions de cours futures ou passées
    // - Dans le body de retour, prévoir un attribut qui indique à l’utilisateur si
    // - le cours est terminé ou non en fonction de la date actuelle
    @Operation(
        summary = "Get all Courses by user Id",
        description = "Retrieve all courses from an User (Coach or simple user) by Id and Indicate the state of the course If its Ended or still Open depending on current Date.")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Course.class), mediaType = "application/json") })})
    @GetMapping("/{userId}/courses")
    public ResponseEntity<Object> getCourses(@PathVariable Integer userId) {
        
        List<Course> courselist = courseService.getAvailableCourses();
        Map<Course, String> courseMessages = new HashMap<>();
        
        if (userService.getByID(userId).isEmpty()){
            return ResponseEntity.ok("No such User id !!");
        }
        else {
            Optional<User> optionaluser = userService.getByID(userId);
            User user = optionaluser.get();
            
            if (!user.getCourses().isEmpty() || (URole.COACH.equals(user.getRole()))) {
                for (Course course : courselist) {
                    LocalDate courseDate = course.getDate(); // Assuming 'Date' is a LocalDate property

                    // Set a message based on the course date or any other condition
                    String message = courseDate.isBefore(LocalDate.now()) ? "ENDED" : "OPEN";
                    if (course.getCoach().getId() == user.getId()){
                        courseMessages.put(course, message);
                    }
                    // Associate each course with its respective message
                    courseMessages.put(course, message);
                }
                return ResponseEntity.ok(courseMessages);
            }
            else {
                System.err.println(courseMessages);
                return ResponseEntity.ok("No Course Found !!");
                }
        }
    }

    
    //  ####################  Annuler son inscription à un cours  #####################################
    // PUT /v1/users/courses/{course_id}/unregister
    // Règles de gestions :  L’utilisateur de type COACH ne peut pas annuler son inscription
    @Operation(
        summary = "Unsubscribe From Course ",
        description = "Simple users can unregister from course by id, since users of Type coach will have an empty [] Courses value So for Coatchs no course will be found.")
    @PutMapping("/courses/{courseId}/unregister")
    public ResponseEntity<Object> unregister( @PathVariable Integer courseId, @RequestParam Integer userId) {
        
        if (userService.getByID(userId).isEmpty()){
            return ResponseEntity.ok("No such User id !!");
        }
        
        else {
            Optional<User> optionalUser = userService.getByID(userId);
            User user = optionalUser.get();
            Set <Course> cours = user.getCourses();
            Course newcourse = null;
            for  (Course cr : cours){
                System.out.println("course name : "+cr.getName());
                if (cr.getId() == courseId){
                    newcourse = cr; // Find course by id and assign it to the new instance newcourse
                    break;
                }
            }
            if (newcourse != null){
                newcourse.resetPlaces(); // When user cancel subscription places++
                courseService.saveCourse(newcourse); // Persist chagement in db
                user.deleteCourse(newcourse); // Delete Course from user courses list and since its a bidirectional relation user will be deleted from course users list
                userService.saveUser(user);
               
                return ResponseEntity.ok("Course Deleted from your Courses List.");
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course Not Found !!");
            }
           
            }
        }

        
    @DeleteMapping
    @Operation(
        summary = "Delete User")
    @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json") })})
    public ResponseEntity<Object> deleteCourse(@RequestParam Integer userId) {
    
        if (userService.getByID(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Found !! !!");
        }
        else {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body("User Deleted.");
        }
    }


}
    

