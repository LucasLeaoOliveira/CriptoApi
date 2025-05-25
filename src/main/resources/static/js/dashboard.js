const api = "http://localhost:8080/api";
const user = JSON.parse(localStorage.getItem('user'));

if (!user) {
    window.location.href = "index.html";
}

const carteiraSelect = document.getElementById("carteiraSelect");
const carteiraOrigemSelect = document.getElementById("carteiraOrigemSelect");
const carteiraDestinoSelect = document.getElementById("carteiraDestinoSelect");
const criptoSelect = document.getElementById("criptoSelect");


function carregarUsuario() {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        document.getElementById('username').textContent = user.nome;
    }
}
// =================== Funções Carteiras ===================

async function carregarCarteiras() {
    const res = await fetch(`${api}/carteiras/usuario/${user.id}`);
    const carteiras = await res.json();

    const div = document.getElementById("carteiras");
    div.innerHTML = "";
    carteiraSelect.innerHTML = "";
    carteiraOrigemSelect.innerHTML = "";
    
    let totalBalance = 0;

    for (const c of carteiras) {
        const saldoRes = await fetch(`${api}/carteiras/${c.id}/saldo-total`);
        const saldo = await saldoRes.json();
        totalBalance += saldo;

        div.innerHTML += `
            <div class="wallet-card">
                <h3>${c.nome}</h3>
                <p>${c.criptomoedas?.[0]?.nome || 'Nenhuma criptomoeda'}</p>
                <p class="balance">$${saldo.toFixed(2)}</p>
                <div class="wallet-actions">
                    <button class="btn-danger" onclick="deletarCarteira(${c.id})">
                        <i class="fas fa-trash"></i> Remover
                    </button>
                </div>
            </div>
        `;

        const option = `<option value="${c.id}">${c.nome} ($${saldo.toFixed(2)})</option>`;
        carteiraSelect.innerHTML += option;
        carteiraOrigemSelect.innerHTML += option;
    }

    // Atualiza o saldo total
    document.getElementById('totalBalance').textContent = `$${totalBalance.toFixed(2)}`;

    // Carrega todas as carteiras para o destino
    const resTodas = await fetch(`${api}/carteiras`);
    const todas = await resTodas.json();
    todas.forEach((c) => {
        carteiraDestinoSelect.innerHTML += `<option value="${c.id}">${c.nome}</option>`;
    });

    carregarCriptos();
}

    

async function criarCarteira() {
    const nome = document.getElementById("nomeCarteira").value;
    if (!nome) return alert("Informe o nome da carteira");

    await fetch(`${api}/carteiras`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome, usuario: { id: user.id } })
    });

    carregarCarteiras();
}

async function deletarCarteira(id) {
    await fetch(`${api}/carteiras/${id}`, { method: "DELETE" });
    carregarCarteiras();
}

// =================== Funções Criptomoedas ===================

async function carregarCriptos() {
    const carteiraId = carteiraSelect.value;
    if (!carteiraId) return;

    const res = await fetch(`${api}/criptomoedas/carteira/${carteiraId}`);
    const criptos = await res.json();

    const div = document.getElementById("criptomoedas");
    div.innerHTML = "";

    criptos.forEach(c => {
        div.innerHTML += `
            <div class="wallet-card">
                <h3>${c.nome}</h3>
                <p>Quantidade: ${c.quantidade}</p>
                <div class="wallet-actions">
                    <button class="btn-danger" onclick="deletarCripto(${c.id})">
                        <i class="fas fa-trash"></i> Remover
                    </button>
                </div>
            </div>
        `;
    });
}

async function adicionarCripto() {
    const carteiraId = carteiraSelect.value;
    const nome = criptoSelect.value;
    const quantidade = document.getElementById("quantidadeCripto").value;

    if (!nome || !quantidade) return alert("Preencha os dados");

    await fetch(`${api}/criptomoedas/carteira/${carteiraId}`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome, quantidade })
    });

    carregarCarteiras();
}

async function deletarCripto(id) {
    await fetch(`${api}/criptomoedas/${id}`, { method: "DELETE" });
    carregarCarteiras();
}

// =================== Transferência Corrigida ===================

async function transferir() {
    const origem = carteiraOrigemSelect.value;
    const destino = carteiraDestinoSelect.value;
    const valorUSD = parseFloat(document.getElementById("valorTransferencia").value);

    if (origem === destino) return alert("Selecione carteiras diferentes");
    if (!valorUSD || valorUSD <= 0) return alert("Informe um valor em dólar válido");

    const criptoRes = await fetch(`${api}/criptomoedas/carteira/${origem}`);
    const cripto = await criptoRes.json();

    if (!cripto.length) {
        alert("A carteira de origem não possui criptomoeda.");
        return;
    }

    const nomeCripto = cripto[0].nome;

    const precoRes = await fetch(`https://api.binance.com/api/v3/ticker/price?symbol=${nomeCripto}USDT`);
    const precoData = await precoRes.json();
    const preco = parseFloat(precoData.price);

    if (!preco || preco <= 0) {
        alert("Erro ao obter preço da criptomoeda.");
        return;
    }

    const quantidade = valorUSD / preco;

    if (cripto[0].quantidade < quantidade) {
        alert("Saldo insuficiente na carteira de origem.");
        return;
    }

    const res = await fetch(`${api}/carteiras/transferir?carteiraOrigemId=${origem}&carteiraDestinoId=${destino}&quantidade=${quantidade}`, {
        method: "POST"
    });

    const text = await res.text();
    alert(text);
    carregarCarteiras();
}

// =================== Lista Criptos da Binance ===================

async function carregarListaCriptosBinance() {
    const res = await fetch("https://api.binance.com/api/v3/ticker/price");
    const data = await res.json();

    const criptos = data
        .filter(c => c.symbol.endsWith("USDT"))
        .map(c => c.symbol.replace("USDT", ""));

    criptoSelect.innerHTML = "";
    criptos.forEach(c => {
        criptoSelect.innerHTML += `<option value="${c}">${c}</option>`;
    });
}

// =================== Logout ===================

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

// =================== Eventos ===================

carteiraSelect.addEventListener("change", carregarCriptos);

// =================== Inicializar ===================
// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    carregarUsuario();
    carregarListaCriptosBinance();
    carregarCarteiras();
});
carregarListaCriptosBinance();
carregarCarteiras();
function mostrarErro(mensagem) {
    const erro = document.createElement('div');
    erro.className = 'error-message';
    erro.textContent = mensagem;
    
    const card = document.querySelector('.card');
    card.insertBefore(erro, card.firstChild);
    
    setTimeout(() => {
        erro.remove();
    }, 5000);
}

async function adicionarCripto() {
    const carteiraId = carteiraSelect.value;
    const nome = criptoSelect.value;
    const quantidade = parseFloat(document.getElementById("quantidadeCripto").value);

    if (!carteiraId) {
        mostrarErro('Selecione uma carteira');
        return;
    }
    if (!nome || !quantidade || quantidade <= 0) {
        mostrarErro('Preencha todos os campos corretamente');
        return;
    }

    try {
        const response = await fetch(`${api}/criptomoedas/carteira/${carteiraId}`, {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, quantidade })
        });

        if (!response.ok) {
            throw new Error('Erro ao adicionar criptomoeda');
        }

        carregarCarteiras();
        document.getElementById("quantidadeCripto").value = '';
    } catch (error) {
        mostrarErro(error.message);
    }
}