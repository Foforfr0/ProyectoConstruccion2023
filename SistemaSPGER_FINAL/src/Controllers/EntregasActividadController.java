/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.EntregaActividadDAO;
import POJO.Actividad;
import POJO.EntregaActividad;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class EntregasActividadController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private GridPane grid;
    
    private Actividad actividad;
    private ArrayList<EntregaActividad> listaEntregas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void ponerDatos(Actividad actividad){
        this.actividad = actividad;
    }
    
    public void cargarListaEntregas(Actividad actividad){
        try {
            listaEntregas = EntregaActividadDAO.obtenerEntregas(this.actividad);
        } catch (Exception e) {
            ShowMessage
                    .showAlertSimple("ERROR",
                            "No se pueden cargar las entregas",
                            Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void btnVolver(ActionEvent event) {
    }
    
    public void cargarActividades(){
        /*int column = 0;
        int row = 0;
        
        for (int i = 0; i < this.listaEntregas.size(); i++) {
            FXMLLoader fxmloader = new FXMLLoader(getClass()
                    .getResource("/Views/ActividadCursoItem.fxml"));
            AnchorPane anchorPane;
            
            try {
                anchorPane = fxmloader.load();
                ActividadCursoItemController itemController = fxmloader
                        .getController();
                itemController.ponerDatos(listaEntregas.get(i));
                
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
        }*/
    }
    
}
