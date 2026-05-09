package org.mastereventos.repository;

import org.mastereventos.model.Rol;
import org.mastereventos.model.Usuario;
import org.mastereventos.singleton.DataStore;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private final List<Usuario> usuarios = DataStore.getInstancia().getUsuarios();

    public List<Usuario> getUsuarios() { return usuarios; }

    public List<Usuario> listarUsuarios() { return new ArrayList<>(usuarios); }

    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.getRol() == rol) resultado.add(u);
        }
        return resultado;
    }

    public Usuario buscarPorCorreo(String correo) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo)) return u;
        }
        return null;
    }

    public Usuario buscarPorId(String idUsuario) {
        for (Usuario u : usuarios) {
            if (u.getIdUsuario().equals(idUsuario)) return u;
        }
        return null;
    }

    public void guardar(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getIdUsuario().equals(usuario.getIdUsuario())) {
                usuarios.set(i, usuario);
                return;
            }
        }
        usuarios.add(usuario);
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void eliminar(String idUsuario) {
        usuarios.removeIf(u -> u.getIdUsuario().equals(idUsuario));
    }

    public String generarNuevoId() {
        int max = usuarios.stream()
                .mapToInt(u -> {
                    try { return Integer.parseInt(u.getIdUsuario().replace("U", "")); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max().orElse(0);
        return "U" + String.format("%03d", max + 1);
    }
}
