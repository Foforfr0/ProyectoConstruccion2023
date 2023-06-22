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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
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
public class AgregarEstudiantesAnteproyectoController implements Initializable {

    @FXML
    private GridPane gridEstudiantesDisponibles;
    private Anteproyecto anteproyecto;
    private ArrayList<Usuario> estudiantesDisponibles;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void cargarEstudiantesDisponibles(){
        int column = 0;
        int row = 1;
        for (int i = 0; i < estudiantesDisponibles.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader(getClass()
                    .getResource("/Views/EstudianteDisponibleItem.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = fxmlloader.load();
                EstudianteDisponibleItemController itemController = fxmlloader
                        .getController();
                itemController.ponerDatos(estudiantesDisponibles.get(i),this.anteproyecto);
                if (column == 1) {
                    column = 0;
                    row ++;
                }
                gridEstudiantesDisponibles.add(anchorPane, column++, row);
                gridEstudiantesDisponibles.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridEstudiantesDisponibles.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridEstudiantesDisponibles.setMaxWidth(Region.USE_PREF_SIZE);
                gridEstudiantesDisponibles.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridEstudiantesDisponibles.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridEstudiantesDisponibles.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            } catch (IOException e) {
                ShowMessage
                        .showAlertSimple("ERROR",
                                "No se puede mostrar la ventana"
                                , Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void ivVolver(MouseEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage)gridEstudiantesDisponibles.getScene().getWindow();
        escenario.close();
    }

    void setDatos(Anteproyecto anteproyectoSeleccionado) {
        this.anteproyecto=anteproyectoSeleccionado;
         try {
            estudiantesDisponibles = UsuarioDAO
                    .consultarEstudiantesDisponibles();
        } catch (SQLException e) {
            ShowMessage.showAlertSimple("ERROR",
                    "Sin conexion con la BD",
                    Alert.AlertType.ERROR);
        }
        cargarEstudiantesDisponibles();
    }
}
