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

        Game game = new Game(new int[3][3],player1, player2,"ongoing", 1, 1);

        //En caso de que el segundo usuario no haya iniciado sesión se pondrá automáticamente el perfil invitado.
        if(player2 != null) {
            game.setPlayer2(player2);
        }

        return gameRepository.save(game);
    }

    public Game makeMove(Game game, User player, int row, int col) {
        if ("finished".equals(game.getGameStatus())) {
            throw new IllegalStateException("La partida ya ha finalizado.");
        }

        int[][] board = game.getBoard();

        // Validar turno
        if ((game.getTurn() == 1 && !game.getPlayer1().getId().equals(player.getId())) ||
                (game.getTurn() == 2 && !game.getPlayer2().getId().equals(player.getId()))) {
            throw new IllegalStateException("No es tu turno.");
        }

        // Validar celda libre
        if (board[row][col] != 0) {
            throw new IllegalArgumentException("Celda ocupada.");
        }

        // Marcar jugada
        board[row][col] = game.getTurn();
        game.setRounds(game.getRounds() + 1);

        // Comprobar si alguien gana
        int winner = checkWinner(board);
        if (winner != 0) {
            game.setGameStatus("finished");
            if (winner == 1) {
                game.getPlayer1().addVictory();
                game.getPlayer2().addDefeat();
            } else {
                game.getPlayer2().addVictory();
                game.getPlayer1().addDefeat();
            }

            // 🔹 Guardar solo al finalizar
            return gameRepository.save(game);
        }

        // Si hay empate
        if (isBoardFull(board)) {
            game.setGameStatus("draw");
            return gameRepository.save(game);
        }

        // Cambiar turno si sigue la partida
        game.setTurn(game.getTurn() == 1 ? 2 : 1);
        return game; // 🔹 No se guarda, solo se devuelve el estado actualizado
    }



    // Comprobar ganador: 0 Empate, 1 Player 1, 2 Player 2
    private int checkWinner(int[][] board) {
        // Revisar filas y columnas
        for (int i = 0; i < 3; i++) {
            // Filas
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
            // Columnas
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }

        // Diagonales
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

        // Nadie ha ganado todavía
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
