/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.AcademicoCuerpoAcademicoDAO;
import POJO.CuerpoAcademico;
import POJO.Usuario;
import Utils.ShowMessage;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ConsultarCAController implements Initializable {

    @FXML
    private Label lbNombreCA;
    @FXML
    private GridPane gridResponsable;
    @FXML
    private GridPane gridIntegrantes;
    
    private ArrayList<Usuario> listaIntegrantes;
    private int idCuerpoAcademico;
    private CuerpoAcademico CuerpoAcademico; 

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            listaIntegrantes = AcademicoCuerpoAcademicoDAO
                    .obtenerAcademicosCuerpoAcademicoIntegrante(idCuerpoAcademico);
            if (listaIntegrantes.isEmpty()) {
                System.out.println("Lista vacia");
            } else {
                System.out.println("No está vacia o.o");
            }
        } catch (Exception e) {
            ShowMessage
                    .showAlertSimple("ERROR",
                            "No se pueden cargar los cursos",
                            Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        
        cargarIntegrantes();
    }    

    @FXML
    private void btnVolver(ActionEvent event) {
        cerrarVentana();
        abrirGestion();
    }
    
    public void setIdCuerpoAcademico(int idCA){
        this.idCuerpoAcademico = idCA;
    }
    
    private void cargarIntegrantes(){
        int column = 0;
        int row = 0;
        
        for (int i = 0; i < this.listaIntegrantes.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader(getClass()
                    .getResource("/Views/CAIntegranteItem.fxml"));
            AnchorPane anchorPane;
            
            try {
                anchorPane = fxmlloader.load();
                CAIntegranteItemController itemController = fxmlloader
                        .getController();
                itemController.ponerDatos(listaIntegrantes.get(i));
                
                if (column == 1) {
                    column = 0;
                    row ++;
                }
                
                gridIntegrantes.add(anchorPane, column++, row);
                gridIntegrantes.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridIntegrantes.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridIntegrantes.setMaxWidth(Region.USE_PREF_SIZE);
                gridIntegrantes.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridIntegrantes.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridIntegrantes.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
                
            } catch (Exception e) {
                e.printStackTrace();
                ShowMessage
                        .showAlertSimple("ERROR",
                                "No se puede mostrar la ventana"
                                , Alert.AlertType.ERROR);
            }
        }
    }
    
    private void abrirGestion(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/GestionCA.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) lbNombreCA.getScene().getWindow();
        escenario.close();
    }
    
    private void ponerTitulo(CuerpoAcademico CA){
        this.CuerpoAcademico = CA;
    }
}
