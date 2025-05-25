const api = "http://localhost:8080/api/auth";

// Login
document.getElementById("loginForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    const res = await fetch(`${api}/login`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
    });

    const user = await res.json();

    if (user?.id) {
        localStorage.setItem("user", JSON.stringify(user));
        window.location.href = "dashboard.html";
    } else {
        alert("Email ou senha inválidos!");
    }
});

// Cadastro
document.getElementById("registerForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    const res = await fetch(`${api}/register`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome, email, senha })
    });

    if (res.ok) {
        alert("Registrado com sucesso!");
        window.location.href = "index.html";
    } else {
        alert("Erro ao registrar");
    }
});

// Logout
function logout() {
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}
