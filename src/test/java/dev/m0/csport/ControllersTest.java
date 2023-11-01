package dev.m0.csport;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import dev.m0.csport.controller.CourseController;
import dev.m0.csport.controller.UserController;
import dev.m0.csport.model.Course;
import dev.m0.csport.model.User;
import dev.m0.csport.service.CourseService;
import dev.m0.csport.service.UserService;

import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@SpringBootTest
@AutoConfigureMockMvc
class ControllersTest {

    @Autowired
    WebApplicationContext context;


	@Autowired
    private MockMvc mockMvc;

    @InjectMocks
    UserController controller;

    @Mock
    private UserService userService;
    @Mock
    private CourseService courseService;
    

    @InjectMocks
    private CourseController courseController;

    @Test
    void testCreateUser() throws Exception {
        String userJson = "{\"fname\": \"testest\",\"lname\": \"test\",\"email\": \"test@example.com\",\"age\": \"22\", \"role\": \"USER\", \"underage\": false}"; // Replace with the JSON representing the User object

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk());
    }
        

    @Test
    public void testListAvailableCourses() throws Exception {
        mockMvc.perform(get("/v1/courses/available")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testRegisterCourse_UserAlreadyRegistred() throws Exception {
        // Arrange
        when(userService.getByID(1)).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(put("/v1/courses/1/register?userId=1"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("User is already registered for this course."));
    }

    @Test
    public void testUnregisterCourse_Success() throws Exception {
        User user = new User();
        user.setId(1);

        Course course = new Course();
        course.setId(1);

        when(userService.getByID(1)).thenReturn(Optional.of(user));
        when(courseService.getByID(1)).thenReturn(Optional.of(course));

        mockMvc.perform(put("/v1/users/courses/1/unregister?userId=1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Course Deleted from your Courses List."));
    }

    @Test
    public void testUnregisterCourse_UserNotFound() throws Exception {
        when(userService.getByID(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/v1/users/courses/1/unregister?userId=99"))
               .andExpect(status().isOk())
               .andExpect(content().string("No such User id !!"));
    }
    
    @Test
    public void testGetCourses_NoUserFound() throws Exception {
        when(userService.getByID(33)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/users/33/courses"))
               .andExpect(status().isOk())
               .andExpect(content().string("No such User id !!"));
    }

}
