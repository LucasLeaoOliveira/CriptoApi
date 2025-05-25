package com.lucas.crypto_api.service;

import com.lucas.crypto_api.model.Carteira;
import com.lucas.crypto_api.model.Criptomoeda;
import com.lucas.crypto_api.repository.CarteiraRepository;
import com.lucas.crypto_api.repository.CriptomoedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarteiraService {

    @Autowired
    private CarteiraRepository carteiraRepo;

    @Autowired
    private CriptomoedaRepository criptoRepo;

    @Autowired
    private BinanceService binanceService;

    public List<Carteira> listarPorUsuario(Long usuarioId) {
        return carteiraRepo.findByUsuarioId(usuarioId);
    }

    public Carteira criar(Carteira carteira) {
        return carteiraRepo.save(carteira);
    }

    public void deletar(Long id) {
        carteiraRepo.deleteById(id);
    }

    public BigDecimal calcularSaldoTotal(Long carteiraId) {
        Carteira carteira = carteiraRepo.findById(carteiraId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        List<Criptomoeda> criptos = criptoRepo.findByCarteiraId(carteiraId);

        if (criptos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        Criptomoeda cripto = criptos.get(0);

        BigDecimal preco = binanceService.buscarPreco(cripto.getNome().toUpperCase());
        return preco.multiply(cripto.getQuantidade());
    }

    public String transferir(Long origemId, Long destinoId, BigDecimal quantidade) {
        Carteira origem = carteiraRepo.findById(origemId)
                .orElseThrow(() -> new RuntimeException("Carteira de origem não encontrada"));

        Carteira destino = carteiraRepo.findById(destinoId)
                .orElseThrow(() -> new RuntimeException("Carteira de destino não encontrada"));

        List<Criptomoeda> origemCriptos = criptoRepo.findByCarteiraId(origemId);
        List<Criptomoeda> destinoCriptos = criptoRepo.findByCarteiraId(destinoId);

        if (origemCriptos.isEmpty()) {
            throw new RuntimeException("A carteira de origem não possui criptomoeda.");
        }

        Criptomoeda origemCripto = origemCriptos.get(0);

        Criptomoeda destinoCripto = destinoCriptos.stream()
                .filter(c -> c.getNome().equalsIgnoreCase(origemCripto.getNome()))
                .findFirst()
                .orElseGet(() -> {
                    Criptomoeda nova = new Criptomoeda();
                    nova.setNome(origemCripto.getNome());
                    nova.setQuantidade(BigDecimal.ZERO);
                    nova.setCarteira(destino);
                    return nova;
                });

        if (origemCripto.getQuantidade().compareTo(quantidade) < 0) {
            throw new RuntimeException("Saldo insuficiente.");
        }

        origemCripto.setQuantidade(origemCripto.getQuantidade().subtract(quantidade));
        destinoCripto.setQuantidade(destinoCripto.getQuantidade().add(quantidade));

        criptoRepo.save(origemCripto);
        criptoRepo.save(destinoCripto);

        return "Transferência realizada com sucesso!";
    }

    public List<Carteira> listarPorCripto(String nomeCripto) {
        List<Carteira> todasCarteiras = carteiraRepo.findAll();
        return todasCarteiras.stream()
                .filter(c -> c.getCriptomoedas().stream()
                        .anyMatch(cripto -> cripto.getNome().equalsIgnoreCase(nomeCripto)))
                .toList();
    }
}
