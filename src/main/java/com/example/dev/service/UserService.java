package com.example.dev.service;

import com.example.dev.dto.request.UserCreationRequest;
import com.example.dev.dto.request.UserUpdateRequest;
import com.example.dev.entity.User;
import com.example.dev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUserRequest(UserCreationRequest request) {
        User user = new User();
        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already exists");

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found bro"));
    }

    public User updateUser(String id, UserUpdateRequest request) {
        User user = getUser(id);
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
