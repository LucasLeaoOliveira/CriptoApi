package com.lucas.crypto_api.repository;

import com.lucas.crypto_api.model.Criptomoeda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CriptomoedaRepository extends JpaRepository<Criptomoeda, Long> {
    
    List<Criptomoeda> findByCarteiraId(Long carteiraId);

    // 🚀 Esse aqui é o que resolve de vez
    Optional<Criptomoeda> findByCarteiraIdAndNomeIgnoreCase(Long carteiraId, String nome);
}
