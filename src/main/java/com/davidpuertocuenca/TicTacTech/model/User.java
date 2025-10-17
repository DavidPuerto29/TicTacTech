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
    private String mail;
    private int phone;
    private Long gamesWon = 0L;
    private Long gamesLost= 0L;
    //Juegos totales suma de (gamesWon + gamesLost)


    public User(String username, String password, String mail, int phone, Long gamesWon, Long gamesLost) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
    }

    public void addVictory(){
        this.gamesWon +=1;
    }

    public void addDefeat(){
        this.gamesLost +=1;
    }

}
