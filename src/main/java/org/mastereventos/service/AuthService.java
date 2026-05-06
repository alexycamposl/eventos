package org.mastereventos.service;

import org.mastereventos.model.Usuario;
import org.mastereventos.repository.UsuarioRepository;

public class AuthService {

    private UsuarioRepository usuarioRepository;

    public AuthService() {
        usuarioRepository = new UsuarioRepository();
    }

    public Usuario login(String correo, String password) {

        Usuario usuario = usuarioRepository.buscarPorCorreo(correo);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }

        return null;
    }
}