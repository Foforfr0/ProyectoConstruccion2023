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
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ActividadesDelCursoController implements Initializable {

    @FXML
    private GridPane gridPaneActividades;
    @FXML
    private Button idBtnVolver;
    
    private Curso curso;
    private ArrayList<Actividad> listaActividades;
    private Timer timer;
    private Timer timer2;
    @FXML
    private Button idBtnConsultar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (curso != null) {
            System.out.println("Se pasó un curso");
        } else {
            System.out.println("No se pasó un curso");
        }
        //cargarActividades();
        
        // Ocultar el botón "Consultar"
        idBtnConsultar.setVisible(false);

        // Crear y configurar el servicio
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(10); // Esperar 0.5 segundos
                        return null;
                    }
                };
            }
        };

        // Configurar el evento "succeeded" del servicio
        service.setOnSucceeded(event -> {
            // Mostrar el botón "Consultar"
            //////idBtnConsultar.setVisible(true);
            // Simular el evento del botón "Consultar"
            idBtnConsultar.fire();
        });

        // Iniciar el servicio
        service.start();
    }    

    @FXML
    private void btnCrearActividad(ActionEvent event) {
        irCrearActividad();
    }

    @FXML
    private void btnVolver(ActionEvent event) {
        regresarPrincipalAcademico();
        cerrarVentana();
    }
    
    private void regresarPrincipalAcademico(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/PrincipalAcademico.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    
    public void cargarActividades(){
        int column = 0;
        int row = 0;
        
        for (int i = 0; i < this.listaActividades.size(); i++) {
            FXMLLoader fxmloader = new FXMLLoader(getClass()
                    .getResource("/Views/ActividadCursoItem.fxml"));
            AnchorPane anchorPane;
            
            try {
                anchorPane = fxmloader.load();
                ActividadCursoItemController itemController = fxmloader
                        .getController();
                itemController.ponerDatos(listaActividades.get(i));
                itemController.ponerCurso(curso);
                
                if (column == 1) {
                    column = 0;
                    row ++;
                }
                gridPaneActividades.add(anchorPane, column++, row);
                gridPaneActividades.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPaneActividades.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridPaneActividades.setMaxWidth(Region.USE_PREF_SIZE);
                gridPaneActividades.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPaneActividades.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPaneActividades.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
                
            } catch (IOException e) {
                ShowMessage
                        .showAlertSimple("ERROR",
                                "No se puede mostrar la ventana"
                                , Alert.AlertType.ERROR);
            }
        }
    }
    
    private void irCrearActividad(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/CrearActividad.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
            
            CrearActividadController controlador = loader.getController();
            controlador.ponerDatos(curso);
            
            cerrarVentana();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana(){
        Stage stage = (Stage)idBtnVolver.getScene().getWindow();
        stage.close();
    }
    
    private void btnGetID(ActionEvent event) {
        if (curso != null) {
            System.err.println("Se paso el curso");
            System.out.println("ID del curso: "+ curso.getIdcurso());
        } else {
            System.err.println("No se paso el curso");
        }
    }
    
    public void ponerDatos(Curso curso){
        this.curso = curso;
    }

    @FXML
    private void btnConsultar(ActionEvent event) {
        if (curso != null) {
            System.out.println("Se pasó un curso");
        } else {
            System.out.println("No se pasó un curso");
        }
        try {
            listaActividades = ActividadDAO.obtenerActividades(curso);
            
        } catch (Exception e) {
            ShowMessage
                    .showAlertSimple("ERROR",
                            "No se pueden cargar las actividades",
                            Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        cargarActividades();
    }
    
    public void cargarLista(Curso curso){
        try {
            ActividadDAO.obtenerActividades(curso);
            listaActividades = ActividadDAO.listaActividades;
            
        } catch (Exception e) {
            ShowMessage
                    .showAlertSimple("ERROR",
                            "No se pueden cargar las actividades",
                            Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    public Curso getCurso(){
        return curso;
    }
}
