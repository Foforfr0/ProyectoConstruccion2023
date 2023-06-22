/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.ActividadDAO;
import POJO.Actividad;
import POJO.Curso;
import Utils.ShowMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ActividadCursoItemController implements Initializable {

    @FXML
    private Label lbNombre;
    
    private Actividad actividad;
    private Curso curso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnEntregas(ActionEvent event) {
        
    }

    @FXML
    private void btnModificar(ActionEvent event) {
        /*Stage escenario = (Stage) lbNombre.getScene().getWindow();
        Scene escena = null;
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/ModificarActividad.fxml"));
        try {
            Parent vista = loader.load();
            ModificarActividadController controlador = loader.getController();
            controlador.prepararActividad(this.actividad);
            controlador.ponerDatos(curso);
            escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Modificar anteproyecto");
        } catch (IOException e) {
            ShowMessage.showAlertSimple("Error", 
                    "Error al cargar la ventana",
                    Alert.AlertType.ERROR);
        }*/
        
        try{
            ActividadDAO.actividadActual = this.actividad;
            ActividadDAO.cursoActual = this.curso;

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/ModificarActividad.fxml"));
            Parent root = loader.load();
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage)this.lbNombre.getScene().getWindow();
            currentStage.close();
        }catch(IOException ioe){
            ShowMessage.showAlertSimple(
                "Error", 
                "Error al abrir la ventana", 
                Alert.AlertType.ERROR
            );
        }
    }
    
    public void ponerDatos(Actividad actividad){
        lbNombre.setText(actividad.getNombre());
        this.actividad = actividad;
    }
    
    public void ponerCurso(Curso curso){
        this.curso = curso;
    }
    
    private void irEntregasActividad(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/EntregasActividad.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
            
            EntregasActividadController controlador = loader.getController();
            controlador.ponerDatos(actividad);
            //controlador.cargarActividades();
            controlador.cargarListaEntregas(actividad);
            
            //cerrarVentana();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
}
