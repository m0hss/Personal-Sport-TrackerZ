package dev.m0.csport.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;




@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema= "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = false)
    private String lname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer age;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private URole role;
    
    // @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "users")
    private Set<Course> courses;

    public void addCourse(Course course) {
        courses.add(course);
        course.getUsers().add(this);
    }
    
    public void deleteCourse(Course course) {
        courses.remove(course);
        course.getUsers().remove(this); // Ensure the bidirectional relationship is updated
    }


    public boolean isUnderage() {
        return age < 18;
    }

    @Override
    public String toString() {
        return "id: " + this.id + ", first name: " + this.fname + ", lname: " + this.lname +", Role: "+ this.role; 
    }
}