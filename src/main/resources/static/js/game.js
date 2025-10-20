const API_URL = "http://localhost:8080/api/games";
let currentGame = null;
let player2Id = null;

const boardEl = document.getElementById("board");
const statusText = document.getElementById("statusText");

// 🔹 Empezar partida
document.getElementById("startGameBtn").addEventListener("click", async () => {
  const player1Id = localStorage.getItem("userId");
  if (!player1Id) {
    alert("❌ Inicia sesión antes de jugar");
    return;
  }

  const player2IdToSend = player2Id || 1; // id 1 = invitado

  const res = await fetch(`${API_URL}/create?player1Id=${player1Id}&player2Id=${player2IdToSend}`, {
    method: "POST"
  });

  if (res.ok) {
    currentGame = await res.json();
    renderBoard();
    statusText.textContent = "🎮 Partida iniciada, turno del Jugador 1";
  } else {
    alert("⚠️ No se pudo crear la partida");
  }
});

// 🔹 Renderizar tablero
function renderBoard() {
  boardEl.innerHTML = "";
  boardEl.classList.remove("hidden");

  for (let i = 0; i < 3; i++) {
    const row = document.createElement("div");
    row.classList.add("row");

    for (let j = 0; j < 3; j++) {
      const cell = document.createElement("div");
      cell.classList.add("cell");
      cell.dataset.row = i;
      cell.dataset.col = j;

      // Pinta si ya hay algo en el tablero
      const value = currentGame.board[i][j];
      if (value === 1) cell.textContent = "X";
      else if (value === 2) cell.textContent = "O";

      cell.addEventListener("click", makeMove);
      row.appendChild(cell);
    }

    boardEl.appendChild(row);
  }
}

async function makeMove(e) {
  // Comprobación de que la partida existe y tiene id
  if (!currentGame || !currentGame.id) {
    return alert("⚠️ No hay partida activa o no tiene ID válido");
  }

    const row = Number(e.target.dataset.row);
    const col = Number(e.target.dataset.col);

  // Validar celda vacía
  if (e.target.textContent !== "") {
    return alert("❌ Esa celda ya está ocupada");
  }

  // Jugador actual
  const movePlayerId = currentGame.turn === 1
    ? Number(localStorage.getItem("userId")) // Jugador 1 siempre logueado
    : Number(player2Id || 1);               // Jugador 2: invitado si no hay sesión iniciada

  if (!movePlayerId) {
    return alert("❌ No hay jugador válido para mover");
  }

console.log("Enviando movimiento:", {
    playerId: movePlayerId,
    row,
    col
  });

console.log("JSON que se envía:", JSON.stringify({
  playerId: Number(movePlayerId),
  row: Number(row),
  col: Number(col)
}));

  try {
    const res = await fetch(`${API_URL}/${currentGame.id}/move`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        playerId: Number(movePlayerId),
        row: Number(row),
        col: Number(col)
      })
    });


    if (!res.ok) {
      const errorMessage = await res.text();
      console.error("Error del servidor:", errorMessage);
      return alert(`❌ Error: ${errorMessage}`);
    }


    // Actualizar estado de juego
    currentGame = await res.json();
    renderBoard();

    // Actualizar front del juego
    if (currentGame.gameStatus === "finished") {
      statusText.textContent = `🏆 ¡Ganó el jugador ${currentGame.turn === 1 ? 2 : 1}!`;
    } else if (currentGame.gameStatus === "draw") {
      statusText.textContent = "🤝 Empate";
    } else {
      statusText.textContent = `Turno del Jugador ${currentGame.turn}`;
    }

  } catch (err) {
    console.error("Error en fetch:", err);
    alert("❌ No se pudo conectar con el servidor");
  }
}



