package com.davidpuertocuenca.TicTacTech.services;

import com.davidpuertocuenca.TicTacTech.model.Game;
import com.davidpuertocuenca.TicTacTech.model.User;
import com.davidpuertocuenca.TicTacTech.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(User player1, User player2) {

        Game game = new Game(new int[3][3],player1, player2,"ongoing", 1, 0);

        //En caso de que el segundo usuario no haya iniciado sesión se pondrá automáticamente el perfil invitado.
        if(player2 != null) {
            game.setPlayer2(player2);
        }

        return gameRepository.save(game);
    }

    public Game makeMove(Game game, User player, int row, int col) {
        if (game.getGameStatus().equals("finished")) {
            throw new IllegalStateException("La partida ya ha finalizado.");
        }

        if ((game.getTurn() == 1 && !game.getPlayer1().getId().equals(player.getId())) ||
                (game.getTurn() == 2 && !game.getPlayer2().getId().equals(player.getId()))) {
            throw new IllegalStateException("No es tu turno.");
        }

        if (game.getBoard()[row][col] != 0) {
            throw new IllegalArgumentException("Celda ocupada.");
        }

        // Guardar el movimiento
        game.getBoard()[row][col] = game.getTurn();

        // Comprobar ganador
        int winner = checkWinner(game.getBoard());
        if (winner != 0) {
            game.setGameStatus("finished");
            if (winner == 1) {
                game.getPlayer1().addVictory();
                //Para evitar guardar datos del invitado.
                if(!game.getPlayer2().getUsername().equals("invitado")) {
                    game.getPlayer2().addDefeat();
                }
            } else if (winner == 2) {
                //Para evitar guardar datos del invitado.
                if(!game.getPlayer2().getUsername().equals("invitado")) {
                    game.getPlayer2().addVictory();
                }
                game.getPlayer1().addDefeat();
            }
        } else if (isBoardFull(game.getBoard())) {
            game.setGameStatus("draw");
        } else {
            game.setTurn(game.getTurn() == 1 ? 2 : 1);
        }

        gameRepository.save(game);

        return game;
    }

    // Comprobar ganador: 0 Empate, 1 Player 1, 2 Player 2
    private int checkWinner(int[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 &&
                    board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] != 0 &&
                    board[0][i] == board[1][i] &&
                    board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }

        if (board[0][0] != 0 &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) {
            return board[0][0];
        }

        if (board[0][2] != 0 &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]) {
            return board[0][2];
        }

        return 0;
    }

    private boolean isBoardFull(int[][] board) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == 0) return false;
        return true;
    }

    public Optional<Game> findById(Long id){
        return gameRepository.findById(id);
    }

}
