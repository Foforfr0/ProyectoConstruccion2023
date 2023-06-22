/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.AnteproyectoDAO;
import POJO.Anteproyecto;
import Utils.ShowMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class AnteproyectoItemController implements Initializable {

    @FXML
    private Label lbNomreAnteproyecto;
    @FXML
    private Label lbEstadoAnteproyecto;
    @FXML
    private Label lbEstado;
    @FXML
    private Button idBtnGestionarEstudiantes;
    @FXML
    private Button idBtnPostular;
    @FXML
    private Button idBtnModificar;
    
    public Anteproyecto anteproyecto;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (anteproyecto != null) {
            System.out.println("Se está pasando el anteproyecto");
        } else {
            System.out.println("No se está pasando en anteproyecto");
        }
    }    

    @FXML
    private void btnModificarAnteproyecto(ActionEvent event) {
        Stage escenarioBase = (Stage) lbNomreAnteproyecto.getScene().getWindow();
        Scene escena = null;
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/EditarAnteproyecto.fxml"));
        try{
            Parent vista = loader.load();
            EditarAnteproyectoController pantallaEditarAnreproyecto = loader.getController();    
            pantallaEditarAnreproyecto.prepararAnteproyecto(this.anteproyecto);
            escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.setTitle("Editar anteproyecto");
        }catch(IOException ex){
            ShowMessage.showAlertSimple("Error", 
                    "Error al cargar la ventana intentelo mas tarde",
                    Alert.AlertType.ERROR);
        }     
    }

    @FXML
    private void btnPostularAnteproyecto(ActionEvent event) {
        try {
            ResultadoOperacion resultado = AnteproyectoDAO
                    .postularAnteproyecto(anteproyecto); 
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Mensaje",
                        resultado.getMensaje(),
                        Alert.AlertType.INFORMATION);
            } else{
                ShowMessage.showAlertSimple("Mensaje",
                        resultado.getMensaje(),
                        Alert.AlertType.ERROR);
            }
        } catch (NullPointerException ex) {
            System.err.println(ex.getMessage());
            ShowMessage.showAlertSimple("Campos faltantes",
                    "Faltan campos en el formulario",
                    Alert.AlertType.WARNING);
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error",
                    "Error en la base de datos",
                    Alert.AlertType.ERROR);
        }
    }
    
    public void ponerDatos(Anteproyecto anteproyecto){
        this.anteproyecto = anteproyecto;
        lbNomreAnteproyecto.setText(anteproyecto.getNombre());
        if (anteproyecto.getEstado() == 1) {
            lbEstadoAnteproyecto.setText("Creado");
            idBtnGestionarEstudiantes.setVisible(false);
        } else if (anteproyecto.getEstado() == 2) {
            lbEstadoAnteproyecto.setText("Postulado");
            idBtnGestionarEstudiantes.setVisible(false);
            idBtnPostular.setVisible(false);
            idBtnModificar.setVisible(false);
        } else if(anteproyecto.getEstado() == 3) {
            lbEstado.setText("Aceptado");
            idBtnModificar.setVisible(false);
            idBtnPostular.setVisible(false);
        } else if (anteproyecto.getEstado() == 4) {
            lbEstado.setText("Rechazado");
            idBtnGestionarEstudiantes.setVisible(false);
            idBtnModificar.setVisible(false);
            idBtnPostular.setVisible(false);
        }
    }

    @FXML
    private void btnGestionarEstudiantes(ActionEvent event) {
        Stage escenarioBase = (Stage) lbNomreAnteproyecto.getScene().getWindow();
        Scene escena = null;
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/GestionarEstudiantesAnteproyecto.fxml"));
        try{
            Parent vista = loader.load();
            GestionarEstudiantesAnteproyectoController gestionarEstudiantes = loader
                    .getController();
            gestionarEstudiantes.setAnteproyectoSeleccionado(this.anteproyecto);
            escena = new Scene(vista);
            escenarioBase.setScene(escena);
        }catch(IOException ex){
            ShowMessage.showAlertSimple("Error", 
                    "Error al cargar la ventana",
                    Alert.AlertType.ERROR);
        };  
    }
    
    public void setAnteproyectoItem(Anteproyecto anteproyecto){
        this.anteproyecto = anteproyecto;
    }
}
