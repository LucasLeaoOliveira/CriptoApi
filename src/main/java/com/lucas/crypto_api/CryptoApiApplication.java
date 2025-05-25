package com.lucas.crypto_api;

import com.lucas.crypto_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class CryptoApiApplication {

    @Autowired
    private UsuarioService usuarioService;

    public static void main(String[] args) {
        SpringApplication.run(CryptoApiApplication.class, args);
    }

    @PostConstruct
    public void init() {
        usuarioService.criaAdmin();
    }
}
