package com.oc.chatop.services;

import com.oc.chatop.models.User;
import com.oc.chatop.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initializeUsers() {
        if (userRepository.count() == 0) {
            User user1 = new User("user1@example.com", "User One", "password123");
            User user2 = new User("user2@example.com", "User Two", "password456");
            User user3 = new User("user3@example.com", "User Three", "password789");

            userRepository.saveAll(List.of(user1, user2, user3));
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserEntityById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}