package com.lucas.crypto_api.service;

import com.lucas.crypto_api.model.Carteira;
import com.lucas.crypto_api.model.Criptomoeda;
import com.lucas.crypto_api.repository.CarteiraRepository;
import com.lucas.crypto_api.repository.CriptomoedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CriptomoedaService {

    @Autowired
    private CriptomoedaRepository criptoRepo;

    @Autowired
    private CarteiraRepository carteiraRepo;

    public List<Criptomoeda> listarPorCarteira(Long idCarteira) {
        Carteira carteira = carteiraRepo.findById(idCarteira).orElse(null);
        return carteira != null ? carteira.getCriptomoedas() : null;
    }

    public Criptomoeda adicionarNaCarteira(Long idCarteira, Criptomoeda cripto) {
        Carteira carteira = carteiraRepo.findById(idCarteira)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        // 🔥 Verificar se já existe essa cripto na carteira
        Optional<Criptomoeda> existenteOpt = criptoRepo.findByCarteiraIdAndNomeIgnoreCase(idCarteira, cripto.getNome());

        if (existenteOpt.isPresent()) {
            // Se existir, soma a quantidade
            Criptomoeda existente = existenteOpt.get();
            BigDecimal novaQuantidade = existente.getQuantidade().add(cripto.getQuantidade());
            existente.setQuantidade(novaQuantidade);
            return criptoRepo.save(existente);
        } else {
            // Se não existir, cria uma nova
            cripto.setCarteira(carteira);
            return criptoRepo.save(cripto);
        }
    }

    public Criptomoeda atualizar(Long id, Criptomoeda atualizado) {
        Criptomoeda existente = criptoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Criptomoeda não encontrada"));

        existente.setNome(atualizado.getNome());
        existente.setQuantidade(atualizado.getQuantidade());

        return criptoRepo.save(existente);
    }

    public void deletar(Long id) {
        criptoRepo.deleteById(id);
    }
}
