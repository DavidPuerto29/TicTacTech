package com.davidpuertocuenca.TicTacTech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private int[][] board = new int[3][3];
    private User player1;
    //Por defecto el segundo jugar es invitado, si inicia sesión se hace un set para añadirlo al game.
    private User player2 = new User("invitado", "invitado", "invitado@hotmail.com", 622222222, 0L, 0L);
    private String gameStatus;
    private int turn;
    private int rounds;

    public Game(int[][] board, User player1, String gameStatus, int turn, int rounds) {
        this.board = board;
        this.player1 = player1;
        this.gameStatus = gameStatus;
        this.turn = turn;
        this.rounds = rounds;
    }
}
