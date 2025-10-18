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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player1_id")
    private User player1;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player2_id")
    //Por defecto el segundo jugador es invitado con id 1, si inicia sesión se hace un set para añadirlo al game.
    private User player2;
    private String gameStatus;
    private int turn;
    private int rounds;

    public Game(int[][] board, User player1, User player2, String gameStatus, int turn, int rounds) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.gameStatus = gameStatus;
        this.turn = turn;
        this.rounds = rounds;
    }
}
