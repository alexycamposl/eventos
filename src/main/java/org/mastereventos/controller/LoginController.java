package org.mastereventos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mastereventos.model.Rol;
import org.mastereventos.model.Usuario;
import org.mastereventos.service.AuthService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String correo = usernameField.getText();
        String password = passwordField.getText();

        AuthService authService = new AuthService();
        Usuario usuario = authService.login(correo, password);

        if (usuario != null) {
            abrirDashboard(usuario);
        } else {
            showAlert("Error", "Correo o contrasena incorrectos");
        }
    }

    private void abrirDashboard(Usuario usuario) {
        try {
            String vista;
            if (usuario.getRol() == Rol.ADMIN) {
                vista = "/org/mastereventos/ui/AdminDashboardView.fxml";
            } else {
                vista = "/org/mastereventos/ui/UserDashboardView.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
            Scene scene = new Scene(loader.load(), 1000, 700);

            if (usuario.getRol() == Rol.ADMIN) {
                AdminDashboardController controller = loader.getController();
                controller.setUsuarioActual(usuario);
            } else {
                UserDashboardController controller = loader.getController();
                controller.setUsuarioActual(usuario);
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MasterEventos - " + usuario.getRol());
            stage.show();

        } catch (Exception e) {
            showAlert("Error", "No se pudo cargar el panel principal");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
