const API_URL = "http://localhost:8080/api/games";
let currentGame = null;
let currentPlayerId = null;
let player2Id = null;

const boardEl = document.getElementById("board");
const statusText = document.getElementById("statusText");

// 🔹 Empezar partida
document.getElementById("startGameBtn").addEventListener("click", async () => {
  // simulamos que ya hay usuario logueado (player1)
  currentPlayerId = localStorage.getItem("userId"); // <- guardado en login

  if (!currentPlayerId) {
    alert("❌ Inicia sesión antes de jugar");
    return;
  }

  const res = await fetch(`${API_URL}/create?player1Id=${currentPlayerId}&player2Id=${player2Id || ""}`, {
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
      cell.addEventListener("click", makeMove);
      row.appendChild(cell);
    }
    boardEl.appendChild(row);
  }
}

// 🔹 Hacer movimiento
async function makeMove(e) {
  if (!currentGame) return alert("⚠️ No hay partida activa");

  const row = e.target.dataset.row;
  const col = e.target.dataset.col;

  const res = await fetch(`${API_URL}/${currentGame.id}/move`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ playerId: currentPlayerId, row, col })
  });

  if (res.ok) {
    currentGame = await res.json();
    e.target.textContent = currentGame.turn === 2 ? "X" : "O"; // cambia turno visualmente
    statusText.textContent = `Turno del Jugador ${currentGame.turn}`;
  } else {
    alert("❌ Movimiento no válido o error del servidor");
  }
}
