package com.lucas.crypto_api.controller;

import com.lucas.crypto_api.model.Carteira;
import com.lucas.crypto_api.repository.CarteiraRepository;
import com.lucas.crypto_api.repository.CriptomoedaRepository;
import com.lucas.crypto_api.service.CarteiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carteiras")
public class CarteiraController {

    @Autowired
    private CarteiraRepository repo;


    @Autowired
    private CarteiraService carteiraService;

    // ✅ Endpoint para listar TODAS as carteiras (resolve o erro no frontend)
    @GetMapping
    public List<Carteira> listarTodas() {
        return repo.findAll();
    }

    @PostMapping
    public Carteira criar(@RequestBody Carteira carteira) {
        return carteiraService.criar(carteira);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Carteira> listarPorUsuario(@PathVariable Long usuarioId) {
        return carteiraService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/por-cripto/{nome}")
    public List<Carteira> listarPorCripto(@PathVariable String nome) {
        return carteiraService.listarPorCripto(nome.toUpperCase());
    }

    @GetMapping("/{id}/saldo-total")
    public BigDecimal saldoTotal(@PathVariable Long id) {
        return carteiraService.calcularSaldoTotal(id);
    }

    @PostMapping("/transferir")
    public String transferir(
            @RequestParam Long carteiraOrigemId,
            @RequestParam Long carteiraDestinoId,
            @RequestParam BigDecimal quantidade) {
        return carteiraService.transferir(carteiraOrigemId, carteiraDestinoId, quantidade);
    }

    // ✅ Endpoint para deletar uma carteira (bom para o frontend)
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        carteiraService.deletar(id);
    }
}
