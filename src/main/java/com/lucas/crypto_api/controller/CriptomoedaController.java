package com.lucas.crypto_api.controller;

import com.lucas.crypto_api.model.Criptomoeda;
import com.lucas.crypto_api.service.CriptomoedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criptomoedas")
public class CriptomoedaController {

    @Autowired
    private CriptomoedaService criptoService;

    @PostMapping("/carteira/{carteiraId}")
    public Criptomoeda adicionar(@PathVariable Long carteiraId, @RequestBody Criptomoeda cripto) {
        return criptoService.adicionarNaCarteira(carteiraId, cripto);
    }

    @GetMapping("/carteira/{carteiraId}")
    public List<Criptomoeda> listar(@PathVariable Long carteiraId) {
        return criptoService.listarPorCarteira(carteiraId);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        criptoService.deletar(id);
    }

    @GetMapping("/disponiveis")
    public List<String> criptosDisponiveis() {
        return List.of("BTC", "ETH", "BNB", "SOL", "ADA", "DOGE");
    }
}
