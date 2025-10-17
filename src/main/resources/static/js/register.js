const API_URL = "http://localhost:8080/api/register";

document.getElementById("registerForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  const confirmPassword = document.getElementById("confirmPassword").value.trim();
  const mail = document.getElementById("mail").value.trim();
  const phone = document.getElementById("phone").value.trim();

  // Validaciones
  if (username.length < 3) return alert("❌ El usuario debe tener al menos 3 caracteres.");
  if (password.length < 4) return alert("❌ La contraseña debe tener al menos 4 caracteres.");
  if (password !== confirmPassword) return alert("❌ Las contraseñas no coinciden.");
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(mail)) return alert("❌ Email inválido.");
  if (!/^\d{6,}$/.test(phone)) return alert("❌ Teléfono inválido. Debe tener al menos 6 dígitos.");

  const newUser = { username, password, mail, phone: parseInt(phone) };

  try {
    const res = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newUser)
    });

    if (res.ok) {
      alert("✅ Registro correcto. Serás redirigido al login.");
      window.location.href = "login.html";
    } else if (res.status === 409) {
      alert("❌ El usuario ya existe. Elige otro nombre de usuario.");
    } else {
      alert("⚠️ Error en el servidor");
    }

  } catch (error) {
    alert("❌ No se pudo conectar con el servidor");
    console.error(error);
  }
});
