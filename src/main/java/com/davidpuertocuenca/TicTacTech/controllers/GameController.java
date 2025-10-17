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

        // Si player2 no existe, se asignará el "invitado" por defecto
        Game game = gameService.createGame(player1.get(), player2.orElse(null));
        return ResponseEntity.ok(game);
    }

    /**
     * Realizar un movimiento
     * POST /api/games/{gameId}/move
     * Body JSON: { "playerId": 1, "row": 0, "col": 1 }
     */
    @PostMapping("/{gameId}/move")
    public ResponseEntity<Game> makeMove(@PathVariable Long gameId, @RequestBody MoveRequest moveRequest) {

        Optional<Game> gameOpt = gameService.findById(gameId);
        Optional<User> playerOpt = userService.findById(moveRequest.getPlayerId());

        if (gameOpt.isEmpty() || playerOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Game updatedGame = gameService.makeMove(
                    gameOpt.get(),
                    playerOpt.get(),
                    moveRequest.getRow(),
                    moveRequest.getCol()
            );
            return ResponseEntity.ok(updatedGame);

        } catch (IllegalStateException | IllegalArgumentException e) {
            // Si hay un error lógico, devolvemos 400 con el mensaje
            return ResponseEntity.badRequest().body(null);

        } catch (Exception e) {
            // Si hay un error inesperado, devolvemos 500
            return ResponseEntity.internalServerError().build();
        }
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
