package com.lucas.crypto_api.service;

import com.lucas.crypto_api.model.Usuario;
import com.lucas.crypto_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    public Usuario criar(Usuario usuario) {
        return repo.save(usuario);
    }

    public Optional<Usuario> login(String email, String senha) {
        Optional<Usuario> user = repo.findByEmail(email);
        if (user.isPresent() && user.get().getSenha().equals(senha)) {
            return user;
        }
        return Optional.empty();
    }

    public void criaAdmin() {
        if (repo.findByEmail("admin@admin.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Admin");
            admin.setEmail("admin@admin.com");
            admin.setSenha("12345");
            repo.save(admin);
        }
    }
}
