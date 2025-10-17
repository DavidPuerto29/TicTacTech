package com.davidpuertocuenca.TicTacTech.repository;

import com.davidpuertocuenca.TicTacTech.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
