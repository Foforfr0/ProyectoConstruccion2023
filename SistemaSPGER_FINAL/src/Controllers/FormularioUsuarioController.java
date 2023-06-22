/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Utils.ShowMessage;
import POJO.RolSistema;
import POJO.Usuario;
import DAO.RolSistemaDAO;
import DAO.UsuarioDAO;
import ConexionBD.ResultadoOperacion;
import exception.SpgerException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;

/**
 * FXML Controller class
 *
 * @author lecap
 */
public class FormularioUsuarioController implements Initializable {

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
    private ComboBox cbTipoUsuario = new ComboBox();
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnRegistrarUsuarioTexto;
    @FXML
    private Button btnCancelarRegistro;
    @FXML
    private Label lbTipoUsuario;
    
    private Usuario usuarioEdicion;
    private ObservableList<RolSistema> listaRolSistema;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaRolesSistema(); 
        configurarTextFieldombre();
        configurarTextFielApellidoP();
        configurarTextFielApellidoM();
        configurarTfMatricula();
        cbTipoUsuario.getSelectionModel().selectFirst();
     }
    
    private void cargarListaRolesSistema(){
        listaRolSistema = FXCollections.observableArrayList();
        try {
            ArrayList<RolSistema> rolSistemaDB = RolSistemaDAO
                    .obtenerRolesSistema();
            listaRolSistema.addAll(rolSistemaDB);
            cbTipoUsuario.setItems(listaRolSistema);
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
  
    @FXML
    private void btnCancelarRegistro(ActionEvent event) {
        try {
            ShowMessage.showAlertSimple("Cancelar",
                        "Registro cancelado",
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
                abrirGestion();
        } catch (Exception e) {   
        }
    }
    
    @FXML
    private void btnRegistrarUsuario(ActionEvent event) {
        int matricula = 0;
        String nombre = tfNombres.getText().trim();
        String apellidoPaterno = tfApellidoPaterno.getText().trim();
        String apellidoMaterno = tfApellidoMaterno.getText().trim();
        String usuario = tfUsername.getText().trim();
        String contraseña = tfPassword.getText().trim();
        RolSistema idRolSistema = (RolSistema)cbTipoUsuario.getValue();
        int tipoUsuario = idRolSistema.getIdRolSistema();
        String matriculaText = tfMatricula.getText();
        
        if (!nombre.isEmpty() && !apellidoPaterno.isEmpty() && !apellidoMaterno.isEmpty()
                && !usuario.isEmpty() && !contraseña.isEmpty() && matriculaText.length() == 8) {
            if (!tfMatricula.getText().isEmpty()) {
                matricula = Integer.parseInt(tfMatricula.getText());
            }
            
            //crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setMatricula(matricula);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidoPaterno(apellidoPaterno);
            nuevoUsuario.setApellidoMaterno(apellidoMaterno);
            nuevoUsuario.setNombreUsuario(usuario);
            nuevoUsuario.setPassword(contraseña);
            nuevoUsuario.setIdRolSistema(tipoUsuario);
            guardarNuevoUsuario(nuevoUsuario);
        } else {
            ShowMessage.showAlertSimple("Formulario",
                    "Faltan campos en el formulario o la matricula no es valida",
                    Alert.AlertType.WARNING);
        }
    }
    
    private void guardarNuevoUsuario(Usuario nuevoUsuario){
        try {
            ResultadoOperacion resultado = UsuarioDAO
                    .registrarUsuario(nuevoUsuario);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Registro", 
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
}
