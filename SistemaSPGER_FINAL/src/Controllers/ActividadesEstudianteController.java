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
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
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
import javafx.scene.control.Label;
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
public class ActividadesEstudianteController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private GridPane gridActividades;
    
    private Curso curso;
    private ArrayList<Actividad> listaActividades;
    private Timer temporizador;
    @FXML
    private Button idBtnxdd;
    @FXML
    private Button idBtnGetId;

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
        
        idBtnxdd.setVisible(false);
        idBtnGetId.setVisible(false);
        //ActEstItemController controladorItem = new ActEstItemController();
        //controladorItem.comprobarEntrega();
        
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
            idBtnxdd.fire();
        });

        // Iniciar el servicio
        service.start();
    }    
    
    public void ponerDatos(Curso curso){
        lbNombre.setText(curso.getNombre());
        this.curso = curso;
    }
    
    @FXML
    private void btnVolver(ActionEvent event) {
        volverMenu();
        cerrarVentana();
    }
    
    private void volverMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/MenuEstudiante.fxml"));
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
    
    private void cerrarVentana(){
        Stage stage = (Stage)lbNombre.getScene().getWindow();
        stage.close();
    }
    
    private void cargarActividades(){
        int column = 0;
        int row = 0;
        
        for (int i = 0; i < this.listaActividades.size(); i++) {
            FXMLLoader fxmloader = new FXMLLoader(getClass()
                    .getResource("/Views/ActEstItem.fxml"));
            AnchorPane anchorPane;
            
            try {
                anchorPane = fxmloader.load();
                //Cambiar a estudiante
                ActEstItemController itemController = fxmloader
                        .getController();
                itemController.ponerDatos(listaActividades.get(i));
                
                if (column == 1) {
                    column = 0;
                    row ++;
                }
                gridActividades.add(anchorPane, column++, row);
                gridActividades.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridActividades.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridActividades.setMaxWidth(Region.USE_PREF_SIZE);
                gridActividades.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridActividades.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridActividades.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
                
            } catch (IOException e) {
                ShowMessage
                        .showAlertSimple("ERROR",
                                "No se puede mostrar la ventana"
                                , Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnGetCurso(ActionEvent event) {
        System.out.println(curso.getIdcurso());
    }

    @FXML
    private void btnConsultarAct(MouseEvent event) {
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

    @FXML
    private void xdddddddd(ActionEvent event) {
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
}
