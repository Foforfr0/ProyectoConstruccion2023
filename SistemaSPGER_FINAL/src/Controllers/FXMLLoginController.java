package Controllers;

import ConexionBD.ConexionBD;
import DAO.UsuarioDAO;
import POJO.Usuario;
import Utils.ShowMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class FXMLLoginController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lbErrorNoPersonal;
    @FXML
    private Label lbErrorPassword;
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tfUsuario.setText("acad1");
        tfPassword.setText("acad1");
    }    

    @FXML
    private void clicBtnIniciarSesion(ActionEvent event) {
        String usuario = tfUsuario.getText();
        String password = tfPassword.getText();
        boolean valido = true;
        lbErrorNoPersonal.setText("");
        lbErrorPassword.setText("");
        if(tfUsuario.getText().isEmpty()){
            valido = false;
            lbErrorNoPersonal.setText("Campo usuario es requerido");
        }
        if(password.isEmpty()){
            valido = false;
            lbErrorPassword.setText("Campo contraseña es requerido");
        }
        if(valido){
            verificarAccesoUsuario(usuario, password);
        }
    }
    
    private void verificarAccesoUsuario(String usuario, String password){
        try {
            Usuario sesion = UsuarioDAO.verificarUsuario(usuario, password);
            
            if (sesion!=null && sesion.getIdUsuario()>0 && sesion.getIdRolSistema()==1) {
                irPantallaPrincipalAdministrador(sesion.toString());
                
            }else if(sesion!=null && sesion.getIdUsuario()>0 && sesion.getIdRolSistema()==2){
                irPantallaPrincipalAcademico(sesion.toString());
                
            }else if(sesion!=null && sesion.getIdUsuario()>0 && sesion.getIdRolSistema()==3){
                irPantallaPrincipalEstudiante(sesion.toString());
                
            }else if(sesion == null){
                ShowMessage.showAlertSimple(
                    "Credenciales incorrectas", 
                    "El usuario y/o contraseña es incorrecto, favor de verificarlos.",
                    Alert.AlertType.WARNING
                );
            }
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple(
                "Error de conexión", 
                "No se pudo establecer conexión con la base de datos.", 
                Alert.AlertType.ERROR
            );
        }catch (NullPointerException nu){
            ShowMessage.showAlertSimple(
                "Error fatal", 
                "Error al validar datos.", 
                Alert.AlertType.ERROR
            );
            nu.printStackTrace();
        }
    }
      
    private void irPantallaPrincipalAdministrador(String nombre){
        try {
            ShowMessage.showAlertSimple(
                    "Bienvenido(a)", 
                    "Credenciales correctas, Bienvenido(a) administrador(a) "+nombre+" al sistema", 
                    Alert.AlertType.INFORMATION);
            Parent vista = FXMLLoader.load(getClass()
                    .getResource("/Views/PrincipalAdministrador.fxml"));
            Scene escenaPrincipal = new Scene(vista);
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessage.showAlertSimple(
                    "Error", 
                    "No se puede mostrar la pantalla principal", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipalAcademico(String nombre){
        try {
            ShowMessage.showAlertSimple(
                    "Bienvenido(a)", 
                    "Credenciales correctas, Bienvenido(a) academico(a) "+nombre+" al sistema", 
                    Alert.AlertType.INFORMATION);
            Parent vista = FXMLLoader.load(getClass()
                    .getResource("/Views/PrincipalAcademico.fxml"));
            Scene escenaPrincipal = new Scene(vista);
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessage.showAlertSimple(
                    "Error", 
                    "No se puede mostrar la pantalla principal", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipalEstudiante(String nombre){
        try {
            ShowMessage.showAlertSimple(
                    "Bienvenido(a)", 
                    "Credenciales correctas, Bienvenido(a) estudiante "+nombre+" al sistema", 
                    Alert.AlertType.INFORMATION);
            Parent vista = FXMLLoader.load(getClass()
                    .getResource("/Views/MenuEstudiante.fxml"));
            Scene escenaPrincipal = new Scene(vista);
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessage.showAlertSimple(
                    "Error", 
                    "No se puede mostrar la pantalla principal", 
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnVerificarConexion(ActionEvent event) {
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion == null) {
            ShowMessage.showAlertSimple("Error", "No hay conexion", Alert.AlertType.WARNING);
        }else{
            ShowMessage.showAlertSimple("Información", "Sí hay conexion", Alert.AlertType.INFORMATION);
        }
    }
}