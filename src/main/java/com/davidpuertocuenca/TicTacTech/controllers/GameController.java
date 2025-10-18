package com.davidpuertocuenca.TicTacTech.controllers;

import com.davidpuertocuenca.TicTacTech.model.Game;
import com.davidpuertocuenca.TicTacTech.model.User;
import com.davidpuertocuenca.TicTacTech.services.GameService;
import com.davidpuertocuenca.TicTacTech.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    /**
     * Crear una nueva partida
     * POST /api/games/create?player1Id=1&player2Id=2
     */
    @PostMapping("/create")
    public ResponseEntity<Game> createGame(@RequestParam Long player1Id, @RequestParam(required = false) Long player2Id) {

        Optional<User> player1 = userService.findById(player1Id);
        Optional<User> player2 = (player2Id != null) ? userService.findById(player2Id) : Optional.empty();

        if (player1.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if(player2.isEmpty()) {
            //En caso de que el segundo usuario no haya iniciado sesión, se le asignara el perfil de invitado no almacenado en la base de datos.
            player2 = userService.findById(1L);
        }

        Game game = gameService.createGame(player1.get(), player2.get());
        return ResponseEntity.ok(game);
    }

    /**
     * Realizar un movimiento
     * POST /api/games/{gameId}/move
     * Body JSON: { "playerId": 1, "row": 0, "col": 1 }
     */
    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody MoveRequest moveRequest) {

        Optional<Game> gameOpt = gameService.findById(gameId);
        Optional<User> playerOpt = userService.findById(moveRequest.getPlayerId());

        if (gameOpt.isEmpty()) return ResponseEntity.status(404).body("Game no encontrado");
        if (playerOpt.isEmpty()) return ResponseEntity.status(404).body("Jugador no encontrado");

        Game game = gameOpt.get();
        User player = playerOpt.get();

        // Validar fila y columna
        if (moveRequest.getRow() < 0 || moveRequest.getRow() > 2 ||
                moveRequest.getCol() < 0 || moveRequest.getCol() > 2) {
            return ResponseEntity.badRequest().body("Fila o columna fuera de rango");
        }

        // Validar turno
        if ((game.getTurn() == 1 && !game.getPlayer1().getId().equals(player.getId())) ||
                (game.getTurn() == 2 && !game.getPlayer2().getId().equals(player.getId()))) {
            return ResponseEntity.badRequest().body("No es tu turno");
        }

        // Validar celda vacía
        if (game.getBoard()[moveRequest.getRow()][moveRequest.getCol()] != 0) {
            return ResponseEntity.badRequest().body("Celda ocupada");
        }

        // Realizar movimiento
        Game updatedGame = gameService.makeMove(game, player, moveRequest.getRow(), moveRequest.getCol());
        return ResponseEntity.ok(updatedGame);
    }


    // DTO interno para recibir el movimiento
    public static class MoveRequest {
        private Long playerId;
        private int row;
        private int col;

        public Long getPlayerId() { return playerId; }
        public void setPlayerId(Long playerId) { this.playerId = playerId; }
        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }
        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }
    }
}
