package org.mastereventos.service;

import org.mastereventos.model.Rol;
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

    public Usuario registrarUsuario(String nombre, String correo, String telefono, String password) {
        // 1. Verificar si el correo ya existe para evitar duplicados
        if (usuarioRepository.buscarPorCorreo(correo) != null) {
            return null; // El usuario ya existe
        }

        String nuevoId = usuarioRepository.generarNuevoId();

        Usuario nuevoUsuario = new Usuario(nuevoId, nombre, correo, telefono, password, Rol.USUARIO);

        usuarioRepository.guardar(nuevoUsuario);

        return nuevoUsuario;
    }
}