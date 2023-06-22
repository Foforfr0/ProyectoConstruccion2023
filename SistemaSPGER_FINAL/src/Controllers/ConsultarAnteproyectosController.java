package Controllers;

import DAO.AnteproyectoDAO;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class ConsultarAnteproyectosController implements Initializable {

    @FXML
    private GridPane gridAnteproyectos;
    @FXML
    private Label lbTitulo;
    private ArrayList<Anteproyecto> anteproyectos;
    private Usuario usuarioActual = UsuarioDAO.getUsuarioActual();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            anteproyectos = AnteproyectoDAO
                    .obtenerAnteproyectos(usuarioActual.getIdUsuario());
        } catch (SQLException e) {
            ShowMessage
                    .showAlertSimple("ERROR", "No se pueden cargar los anteproyectos"
                            , Alert.AlertType.ERROR);
        }
        cargarAnteproyectosPostulados();
    }
    
    private void cargarAnteproyectosPostulados(){
        if(!this.anteproyectos.isEmpty()){
        };
        int column = 0;
        int row = 1;
        for (int i = 0; i < this.anteproyectos.size(); i++) {
            FXMLLoader fxmloader = new FXMLLoader(getClass()
                    .getResource("/Views/AnteproyectoItem.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = fxmloader.load();
                AnteproyectoItemController itemController = fxmloader
                        .getController();
                itemController.ponerDatos(anteproyectos.get(i));
                itemController.setAnteproyectoItem(anteproyectos.get(i));
                if (column == 1) {
                    column = 0;
                    row ++;
                }
                gridAnteproyectos.add(anchorPane, column++, row);
                gridAnteproyectos.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridAnteproyectos.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridAnteproyectos.setMaxWidth(Region.USE_PREF_SIZE);
                gridAnteproyectos.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridAnteproyectos.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridAnteproyectos.setMaxHeight(Region.USE_PREF_SIZE);
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
        irPrincipalAcademico();
    }
    
    private void irPrincipalAcademico(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/PrincipalAcademico.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage escenarioBase = (Stage) lbTitulo.getScene().getWindow();
        escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    
    private void cerrarVentana(){
        Stage x = (Stage) lbTitulo.getScene().getWindow();
        x.close();
    }
}
