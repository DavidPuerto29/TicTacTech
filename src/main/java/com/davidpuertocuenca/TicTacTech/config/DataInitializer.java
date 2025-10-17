package com.davidpuertocuenca.TicTacTech.config;

import com.davidpuertocuenca.TicTacTech.model.User;
import com.davidpuertocuenca.TicTacTech.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    //En caso de que el usuario invitado no exista, se añade a la base de datos. Esta comprobación se realiza siempre que se ejecuta el programa.
    @Override
    public void run(String... args) throws Exception {
        Optional<User> invitado = userService.findByUsername("invitado");
        if (invitado.isEmpty()) {
            invitado = Optional.of(new User( "invitado", "invitado", "invitado@hotmail.com", 123456789, 0L, 0L));
                userService.saveUser(invitado.get());
        }
    }
}
