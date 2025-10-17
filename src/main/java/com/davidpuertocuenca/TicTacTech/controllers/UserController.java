package com.davidpuertocuenca.TicTacTech.controllers;

import com.davidpuertocuenca.TicTacTech.model.User;
import com.davidpuertocuenca.TicTacTech.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User loggedUser = userService.userLogin(user.getUsername(),user.getPassword());

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // TEST BORRA CONTRASENA
        loggedUser.setPassword(null);

        return ResponseEntity.ok(loggedUser);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = userService.saveUser(user);

        if (newUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        // TEST BORRA CONTRASENA
        newUser.setPassword(null);

        return ResponseEntity.ok(newUser);
    }


}
