package com.example.Project_VuDucTrung.service;


import com.example.Project_VuDucTrung.model.User;
import com.example.Project_VuDucTrung.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUserNameAndPassWord(username,password);
        return user;
    }
}
