package com.lucas.crypto_api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class BinanceService {

    public BigDecimal buscarPreco(String simbolo) {
        try {
            String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + simbolo + "USDT";
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.get("price") != null) {
                return new BigDecimal(response.get("price"));
            }

            throw new RuntimeException("Não foi possível obter o preço da Binance.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar preço na Binance: " + e.getMessage());
        }
    }
}
