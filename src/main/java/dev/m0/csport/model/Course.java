package dev.m0.csport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;


import com.fasterxml.jackson.annotation.JsonBackReference;




@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course", schema= "public")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String localisation;
    
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CType type;

    @Column(nullable = true)
    private boolean available;

    @Column(nullable = false)
    private Integer places;

    @ManyToOne
    @JoinColumn(name = "coach", referencedColumnName = "id")
    private User coach;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_courses",
            joinColumns = @JoinColumn(name = "courseid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "userid", referencedColumnName = "id")
    )
    private Set<User> users;

    @Override
    public String toString() {
        return "ID: "+ this.id+ ", Name: " + this.name + ", Localisation: " + this.localisation + ", Coach: " + this.coach.getLname() +", Type(Inside, Outside): "+ this.type + ", Available Places: " +this.places; 
    }

    public boolean isFull() {
        return places <= 0;
    }

    public void updatePlaces(){
        if (places > 0){
            places--;
        }
    }

    public void addUser(User user) {
        users.add(user);
        user.getCourses().add(this);
    }

    public void resetPlaces() {
        int currentplaces = this.getPlaces();
        this.setPlaces(currentplaces + 1);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course otherCourse = (Course) o;
        return Objects.equals(id, otherCourse.id); // Compare courses by their ID (or any other unique identifier)
    }

}