package com.lucas.crypto_api.controller;

import com.lucas.crypto_api.model.Usuario;
import com.lucas.crypto_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
        return usuarioService.criar(usuario);
    }

    @PostMapping("/login")
    public Optional<Usuario> login(@RequestBody Usuario login) {
        return usuarioService.login(login.getEmail(), login.getSenha());
    }
}
