package org.mastereventos.repository;

import java.util.ArrayList;
import java.util.List;

import org.mastereventos.model.Rol;
import org.mastereventos.model.Usuario;

public class UsuarioRepository {

    private List<Usuario> usuarios;

    public UsuarioRepository() {
        usuarios = new ArrayList<>();

        usuarios.add(new Usuario(
                "1",
                "Administrador",
                "admin@mastereventos.com",
                "3000000000",
                "1234",
                Rol.ADMIN
        ));

        usuarios.add(new Usuario(
                "2",
                "Usuario General",
                "user@mastereventos.com",
                "3111111111",
                "1234",
                Rol.USUARIO
        ));
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario buscarPorCorreo(String correo) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo)) {
                return usuario;
            }
        }
        return null;
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
}