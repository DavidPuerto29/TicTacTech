const API_URL = "http://localhost:8080/api/games";
let currentGame = null;
let currentPlayerId = null;
let player2Id = null;

const boardEl = document.getElementById("board");
const statusText = document.getElementById("statusText");

// 🔹 Empezar partida
document.getElementById("startGameBtn").addEventListener("click", async () => {
  const player1Id = localStorage.getItem("userId"); // jugador 1 logueado
  if (!player1Id) {
    alert("❌ Inicia sesión antes de jugar");
    return;
  }

  // jugador 2: si hay session usar su id, sino invitado (id 1)
  const player2IdToSend = player2Id || 1;

  const res = await fetch(`${API_URL}/create?player1Id=${player1Id}&player2Id=${player2IdToSend}`, {
    method: "POST"
  });

  if (res.ok) {
    currentGame = await res.json();
    currentPlayerTurn = 1; // empieza jugador 1
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
      cell.addEventListener("click", makeMove);
      row.appendChild(cell);
    }
    boardEl.appendChild(row);
  }
}

// 🔹 Hacer movimiento
async function makeMove(e) {
  if (!currentGame) return alert("⚠️ No hay partida activa");

   const row = Number(e.target.dataset.row);
   const col = Number(e.target.dataset.col);

  // asignamos el playerId correcto según el turno
  const movePlayerId = currentGame.turn === 1
    ? localStorage.getItem("userId") // jugador 1 siempre logueado
    : (player2Id || 1);              // jugador 2: invitado si no hay sesión

    const res = await fetch(`${API_URL}/${currentGame.id}/move`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        playerId: Number(movePlayerId),
        row,
        col
      })
    });

  if (res.ok) {
    currentGame = await res.json();
    e.target.textContent = currentGame.turn === 2 ? "X" : "O"; // cambiar visualmente
    statusText.textContent = `Turno del Jugador ${currentGame.turn}`;
  } else {
    alert("❌ Movimiento no válido o error del servidor");
  }
}

