/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import DAO.RolSistemaDAO;
import POJO.Usuario;
import DAO.UsuarioDAO;
import POJO.RolSistema;
import Utils.ShowMessage;
import ConexionBD.ResultadoOperacion;
import interfaces.RespuestaFormulario;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author lecap
 */
public class GestionUsuariosController implements Initializable {

    @FXML
    private ComboBox cmbTipoUsuario = new ComboBox<>();
    @FXML
    private TableView tableUsuarios;
    private TableColumn colIDusuario;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colTipoUsuario;
    @FXML
    private Button btnNuevoUsuarioid;
    
    ObservableList<Usuario> listaUsuarios;
    ObservableList<RolSistema> listaRolSistema;
    ObservableList<Usuario> listaPrueba;
    private RespuestaFormulario notificacion;
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarListaRolesSistema();
        cmbTipoUsuario.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, 
                newValue) -> {
            if (newValue != null) {
                if (newValue.equals(listaRolSistema.get(0))) {
                    cargarDatosAdministradores();
                } else if(newValue.equals(listaRolSistema.get(1))) {
                    cargarDatosAcademicos();
                } else if(newValue.equals(listaRolSistema.get(2))){
                    cargarDatosEstudiantes();
                }else {
                }
            }
        });
        
        cmbTipoUsuario.getSelectionModel().selectFirst();
    }    

    @FXML
    private void btnVolver(ActionEvent event) throws IOException{
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("/Views/PrincipalAdministrador.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        cerrarVentana();
    }

    @FXML
    private void btnNuevoUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/FormularioUsuario.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            cerrarVentana();
        } catch (Exception e) {
        } 
    }
    
    @FXML
    private void btnEliminarUsuario(ActionEvent event) {
        Usuario usuarioEliminacion = verificarUsuarioSeleccionado();
        Usuario sesion = UsuarioDAO.getUsuarioActual();
        if (usuarioEliminacion != null && usuarioEliminacion.getIdUsuario() != sesion.getIdUsuario()) {
            boolean eliminar = ShowMessage.showConfirmationDialog("Eliminar usuario", 
                    "¿Desea eliminar el usuario " + usuarioEliminacion.toString() + "?");
            if (eliminar) {
                try {
                    ResultadoOperacion resultado = UsuarioDAO.eliminarUsuario(usuarioEliminacion.getIdUsuario());
                    if (!resultado.isError() && usuarioEliminacion.getIdRolSistema() == 1) {
                        ShowMessage.showAlertSimple("Usuario eliminado", 
                                "El usuario se ha eliminado correctamente", Alert.AlertType.INFORMATION);
                        cargarDatosAdministradores();
                      System.out.println("se cargo datos estudiante");
                    }else if(!resultado.isError() && usuarioEliminacion.getIdRolSistema() == 2){
                        ShowMessage.showAlertSimple("Usuario eliminado", 
                                "El usuario se ha eliminado correctamente", Alert.AlertType.INFORMATION);
                        cargarDatosAcademicos();
                        System.out.println("se cargo datos estudiante");
                    }else if(!resultado.isError() && usuarioEliminacion.getIdRolSistema() == 3){
                        ShowMessage.showAlertSimple("Usuario eliminado", 
                                "El usuario se ha eliminado correctamente", Alert.AlertType.INFORMATION);
                        cargarDatosEstudiantes();
                        System.out.println("se cargo datos estudiante");
                    }else {
                        System.out.println("No está entrando al if");
                        System.out.println(usuarioEliminacion.getIdRolSistema());
                        System.out.println(usuarioEliminacion.getIdUsuario());
                    }         
                } catch (SQLException e) {
                    ShowMessage.showAlertSimple("Error", e.getMessage(), 
                            Alert.AlertType.ERROR);
                }
            }
        } else {
            ShowMessage.showAlertSimple("Selecciona un usuario", 
                    "Debes seleccionar un usuario valido", Alert.AlertType.WARNING);
        }
    }
    
    private Usuario verificarUsuarioSeleccionado(){
        int filaSellecionada = tableUsuarios.getSelectionModel().getSelectedIndex();
        return (filaSellecionada >= 0 ) ? listaUsuarios.get(filaSellecionada) : null;
    }

    @FXML
    private void btnModificarUsuario(ActionEvent event) {
        Usuario usuarioEdicion = verificarUsuarioSeleccionado();
        if (usuarioEdicion != null) {
            irFormulario(usuarioEdicion);
        }else{
            ShowMessage.showAlertSimple("Seleccione un usuario", 
                    "Debe seleccionar un usuario", Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(Usuario usuario){
        String ruta = "/Views/ModificarUsuario.fxml";
        cerrarVentana();
        try {
            FXMLLoader controladorAcceso = new FXMLLoader(getClass()
                    .getResource(ruta));
            Parent vista = controladorAcceso.load();
            ModificarUsuarioController modificarUsuario = controladorAcceso
                    .getController();
            modificarUsuario.iniciarEdicion(usuario);
            Scene escenaEdicion = new Scene(vista);
            Stage escenarioNuevo = new Stage();
            escenarioNuevo.setScene(escenaEdicion);
            escenarioNuevo.initModality(Modality.APPLICATION_MODAL);
            escenarioNuevo.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    private void cargarListaRolesSistema(){
        listaRolSistema = FXCollections.observableArrayList();
        try {
            ArrayList<RolSistema> rolSistemaDB = RolSistemaDAO.obtenerRolesSistema();
            listaRolSistema.addAll(rolSistemaDB);
            cmbTipoUsuario.setItems(listaRolSistema);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    private void configurarTabla(){
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
            colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
            colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
            colTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("prueba"));
    }
     
    private void cargarDatosAdministradores(){
        try {
            listaUsuarios = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioBD = UsuarioDAO.obtenerUsuariosAdministradores();
            listaUsuarios.addAll(usuarioBD);
            tableUsuarios.setItems(listaUsuarios);
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta"
                    , Alert.AlertType.ERROR);
        }catch (NullPointerException n){
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio"
                    , Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDatosAcademicos(){
        try {
            listaUsuarios = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioBD = UsuarioDAO.obtenerUsuariosAcademicos();
            listaUsuarios.addAll(usuarioBD);
            tableUsuarios.setItems(listaUsuarios);
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta", Alert.AlertType.ERROR);
        }catch (NullPointerException n){
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDatosEstudiantes(){
        try {
            listaUsuarios = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioBD = UsuarioDAO.obtenerUsuariosEstudiantes();
            listaUsuarios.addAll(usuarioBD);
            tableUsuarios.setItems(listaUsuarios);
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta", Alert.AlertType.ERROR);
        }catch (NullPointerException n){
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio", Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) btnNuevoUsuarioid.getScene().getWindow();
        escenario.close();
    }
}
