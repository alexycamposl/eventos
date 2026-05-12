package org.mastereventos.controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import java.io.IOException;
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
import javafx.event.ActionEvent;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private TextField loginCorreoField;
    @FXML private PasswordField loginPasswordField;

    @FXML private TextField regNombreField;
    @FXML private TextField regCorreoField;
    @FXML private TextField regTelefonoField;
    @FXML private PasswordField regPasswordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String correo = loginCorreoField.getText();
        String password = loginPasswordField.getText();

        if (correo.isBlank() || password.isBlank()) {
            mostrarAlerta("Error", "Por favor ingresa correo y contraseña.", Alert.AlertType.WARNING);
            return;
        }

        Usuario usuarioAutenticado = authService.login(correo, password);

        if (usuarioAutenticado != null) {
            System.out.println("Login exitoso para: " + usuarioAutenticado.getNombre());

            try {
                String fxmlFile = "";
                if (usuarioAutenticado.getRol() == Rol.ADMIN) {
                    fxmlFile = "/org/mastereventos/ui/AdminDashboardView.fxml";
                } else {
                    fxmlFile = "/org/mastereventos/ui/UserDashboardView.fxml";
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();

                // --- NUEVO CÓDIGO: PASAR LOS DATOS AL CONTROLADOR ---
                if (usuarioAutenticado.getRol() == Rol.USUARIO) {
                    // Obtenemos el controlador de la ventana que acabamos de cargar
                    UserDashboardController userController = loader.getController();
                    // Le enviamos el usuario
                    userController.setUsuarioLogueado(usuarioAutenticado);
                }
                // Si quieres hacer lo mismo para el Admin, podrías añadir un 'else if' aquí luego.
                // ----------------------------------------------------

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 700);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

            }catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error Fatal", "No se pudo cargar la interfaz del sistema.", Alert.AlertType.ERROR);
            }

        } else {
            mostrarAlerta("Error de autenticación", "Correo o contraseña incorrectos.", Alert.AlertType.ERROR);
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

    // Asegúrate de tener tu servicio instanciado
    private AuthService authService = new AuthService();

    // ... (Mantén tu método handleLogin actual, solo recuerda usar loginCorreoField.getText()) ...

    // 2. Nuevo método para manejar el registro
    @FXML
    public void handleRegistro(ActionEvent event) {
        String nombre = regNombreField.getText();
        String correo = regCorreoField.getText();
        String telefono = regTelefonoField.getText();
        String password = regPasswordField.getText();

        // Validación básica de campos vacíos
        if (nombre.isBlank() || correo.isBlank() || telefono.isBlank() || password.isBlank()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios para registrarse.", Alert.AlertType.WARNING);
            return;
        }

        // Llamada a nuestro nuevo método en el servicio
        Usuario nuevoUsuario = authService.registrarUsuario(nombre, correo, telefono, password);

        if (nuevoUsuario != null) {
            mostrarAlerta("Éxito", "Cuenta creada exitosamente. ¡Ahora puedes iniciar sesión!", Alert.AlertType.INFORMATION);

            // Limpiar los campos después del registro exitoso
            regNombreField.clear();
            regCorreoField.clear();
            regTelefonoField.clear();
            regPasswordField.clear();

            // Opcional: pre-llenar el correo en la pestaña de login para mayor comodidad
            loginCorreoField.setText(correo);
        } else {
            mostrarAlerta("Error", "El correo " + correo + " ya está registrado en el sistema.", Alert.AlertType.ERROR);
        }
    }

    // Método de utilidad para alertas (si no lo tenías ya)
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
