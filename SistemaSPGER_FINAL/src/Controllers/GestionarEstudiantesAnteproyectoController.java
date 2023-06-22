/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.UsuarioDAO;
import POJO.Anteproyecto;
import POJO.Usuario;
import Utils.ShowMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class GestionarEstudiantesAnteproyectoController implements Initializable {

    @FXML
    private Button idBtnAgregarEstudiante;
    @FXML
    private GridPane gridEstudiantesAnteproyecto;
    private ArrayList<Usuario> listaEstudiantes;
    private Anteproyecto anteproyectoSeleccionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }  
    private void cargarAnteproyectos(){
        // TODO
        System.out.println("Iniciando la ventana");
        try {
            FXMLLoader fxmloader = new FXMLLoader(getClass()
                    .getResource("/Views/AnteproyectoItem.fxml"));
            AnteproyectoItemController itemController = fxmloader.getController();
            this.listaEstudiantes = UsuarioDAO
                    .obtenerEstudiantesAnteproyecto(anteproyectoSeleccionado);
            System.out.println(listaEstudiantes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cargarEstudianteAnteproyecto();
    }

    @FXML
    private void btnAgregarEstudiante(ActionEvent event) {
        //TODO
        irEstudiantesDisponibles();
        
    }   
    
    private void cargarEstudianteAnteproyecto(){
        if (this.listaEstudiantes.isEmpty()) {
            //myListener
        };
        int column = 0;
        int row = 0;
        for (int i = 0; i < this.listaEstudiantes.size(); i++) {            
            FXMLLoader fxmlloader = new FXMLLoader(getClass()
                    .getResource("/Views/EstudianteAnteproyectoItem.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = fxmlloader.load();
                EstudianteAnteproyectoItemController itemController = fxmlloader
                        .getController();
                itemController.ponerDatos(listaEstudiantes.get(i),this.anteproyectoSeleccionado);
                if (column == 1) {
                    column = 0;
                    row++;
                }
                gridEstudiantesAnteproyecto.add(anchorPane, column++, row);
                gridEstudiantesAnteproyecto.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridEstudiantesAnteproyecto.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridEstudiantesAnteproyecto.setMaxWidth(Region.USE_PREF_SIZE);
                gridEstudiantesAnteproyecto.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridEstudiantesAnteproyecto.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridEstudiantesAnteproyecto.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            } catch (IOException e) {
                ShowMessage.showAlertSimple("ERROR",
                        "No se puede mostrar la lista de estudiantes",
                        Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void ivVolverClic(MouseEvent event) {
        irConsultarAnteproyectos();
    }
    
    private void irConsultarAnteproyectos(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/ConsultarAnteproyectos.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage escenarioBase = (Stage) idBtnAgregarEstudiante.getScene().getWindow();
            escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    public void irEstudiantesDisponibles(){
        try {
            //abrir la ventana
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/AgregarEstudiantesAnteproyecto.fxml"));
            Parent root = loader.load();
            AgregarEstudiantesAnteproyectoController itemController=loader.getController();
            System.out.println(this.anteproyectoSeleccionado+ "GESTIONAR ESTUDIANTES");
            itemController.setDatos(this.anteproyectoSeleccionado);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede abrir la ventana", 
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    public void setAnteproyectoSeleccionado(Anteproyecto anteproyecto){
        this.anteproyectoSeleccionado = anteproyecto;
        cargarAnteproyectos();
    }
}
