package com.davidpuertocuenca.TicTacTech.services;

import com.davidpuertocuenca.TicTacTech.model.User;
import com.davidpuertocuenca.TicTacTech.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User userLogin(String username, String password){
       User user  = userRepository.findByUsername(username).orElse(null);

       if(user != null && user.getPassword().equals(password)){
           return user;
       }
       return null;
    }
}
