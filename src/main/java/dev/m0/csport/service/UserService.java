package dev.m0.csport.service;

import dev.m0.csport.model.User;
import dev.m0.csport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Vous devrez créer ce repository.

    static List<User> users = new ArrayList<User>();

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public Optional<User> getByID(Integer userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
    
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
      }

}
