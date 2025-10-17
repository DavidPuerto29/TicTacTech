const API_URL = "http://localhost:8080/api/login";

document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  try {
    const res = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    if (res.ok) {
      const user = await res.json();
      alert(`✅ Login correcto. Bienvenido ${user.username}!`);
      window.location.href = "game.html";
    } else if (res.status === 401) {
      alert("❌ Usuario o contraseña incorrectos");
    } else {
      alert("⚠️ Error en el servidor");
    }

  } catch (error) {
    alert("❌ No se pudo conectar con el servidor");
    console.error(error);
  }
});
