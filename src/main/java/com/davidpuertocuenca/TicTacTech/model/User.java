package com.davidpuertocuenca.TicTacTech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Long gamesWon;
    private Long gamesLost;
    //Juegos totales suma de (gamesWon + gamesLost)


    public User(Long id, String username, String password, Long gamesWon, Long gamesLost) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
    }

    private void addVictory(){
        this.gamesWon +=1;
    }

    private void addDefeat(){
        this.gamesLost +=1;
    }

}
