/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.UsuarioDAO;
import POJO.Usuario;
import Utils.ShowMessage;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ModificarUsuarioController implements Initializable {

    @FXML
    private Label lbTituloFormulario;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfNombres;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfUsername;
    @FXML
    private Button btnCancelarRegistro;
    @FXML
    private Button btnGuardarCambiosText;
    @FXML
    private PasswordField tfPassword;
    
    private Usuario usuarioEditable;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTextFieldombre();
        configurarTextFielApellidoP();
        configurarTextFielApellidoM();
        configurarTfMatricula();
    }    

    @FXML
    private void btnCancelarRegistro(ActionEvent event) {
        try {
            ShowMessage.showAlertSimple("Registro",
                        "Modificacion cancelada",
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
                abrirGestion();
        } catch (Exception e) {   
        }
    }
    
    @FXML
    private void btnGuardarCambios(ActionEvent event) {
        int matricula = 0;
        String nombre = tfNombres.getText();
        String apellidoPaterno = tfApellidoMaterno.getText();
        String apellidoMaterno = tfApellidoMaterno.getText();
        String usuario = tfUsername.getText();
        String contraseña = tfPassword.getText();
        String matriculaText = tfMatricula.getText();
        
        if (!nombre.isEmpty() && !apellidoPaterno.isEmpty() && !apellidoMaterno.isEmpty()
                && !usuario.isEmpty() && !contraseña.isEmpty() && matriculaText.length() == 8) {
            if (!tfMatricula.getText().isEmpty()) {
                matricula = Integer.parseInt(tfMatricula.getText());
            }
            
            usuarioEditable.setMatricula(matricula);
            usuarioEditable.setNombre(nombre);
            usuarioEditable.setApellidoPaterno(apellidoPaterno);
            usuarioEditable.setApellidoMaterno(apellidoMaterno);
            usuarioEditable.setNombreUsuario(usuario);
            usuarioEditable.setPassword(contraseña);
            
            guardarEdicion(usuarioEditable);
        } else {
            ShowMessage.showAlertSimple("Formulario",
                    "Faltan campos en el formulario o la matricula no es valida",
                    Alert.AlertType.WARNING);
        } 
    }
    
    public void iniciarEdicion(Usuario usuario){
        this.usuarioEditable = usuario;
        if (usuario != null) {
            iniciarDatos();
        }
    }
    
    private void iniciarDatos(){
        tfMatricula.setText(String.valueOf(usuarioEditable.getMatricula()).trim());
        tfNombres.setText(usuarioEditable.getNombre().trim());
        tfApellidoPaterno.setText(usuarioEditable.getApellidoPaterno().trim());
        tfApellidoMaterno.setText(usuarioEditable.getApellidoMaterno().trim());
        tfUsername.setText(usuarioEditable.getNombreUsuario().trim());
        tfPassword.setText(usuarioEditable.getPassword().trim());
    }
    
    private void guardarEdicion(Usuario usuarioEditado){
        try {
            ResultadoOperacion resultado = UsuarioDAO
                    .guardarEdicion(usuarioEditado);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Mensaje", 
                        resultado.getMensaje(), 
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
                abrirGestion();
            } 
        } catch (SQLIntegrityConstraintViolationException ex) {
            if (ex.getMessage().contains("usuario.username_UNIQUE")) {
                ShowMessage.showAlertSimple("Error",
                        "El nombre de usuario ya se encuentra registrado",
                        Alert.AlertType.WARNING);
            } else if (ex.getMessage().contains("usuario.matricula_UNIQUE")) {
                ShowMessage.showAlertSimple("Error",
                        "La matricula ya se encuentra registrada",
                        Alert.AlertType.WARNING);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ShowMessage.showAlertSimple("Error",
                        "Error en la base de datos",
                        Alert.AlertType.WARNING);
        }
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) tfNombres.getScene().getWindow();
        escenario.close();
    }
    
    private void abrirGestion(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/GestionUsuarios.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void configurarTextFieldombre(){
        Pattern pattern = Pattern.compile("[a-zA-Z ]*");
        
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change; // Aceptar cambios válidos (solo letras)
            }
            return null; // Rechazar cambios inválidos
        };
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tfNombres.setTextFormatter(textFormatter);
    }
    
    private void configurarTextFielApellidoP(){
        Pattern pattern = Pattern.compile("[a-zA-Z ]*");
        
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change; // Aceptar cambios válidos (solo letras)
            }
            return null; // Rechazar cambios inválidos
        };
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tfApellidoPaterno.setTextFormatter(textFormatter);
    }
    
    private void configurarTextFielApellidoM(){
        Pattern pattern = Pattern.compile("[a-zA-Z ]*");
        
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change; // Aceptar cambios válidos (solo letras)
            }
            return null; // Rechazar cambios inválidos
        };
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tfApellidoMaterno.setTextFormatter(textFormatter);
    }
    
    private void configurarTfMatricula(){
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d{0,8}")) {
            return change; // Aceptar cambios válidos de máximo 8 caracteres numéricos
        }
        return null; // Rechazar cambios inválidos
        });
        tfMatricula.setTextFormatter(textFormatter);
    }
}
