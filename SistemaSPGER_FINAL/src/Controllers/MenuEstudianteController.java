/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.CursoDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import DAO.UsuarioDAO;
import POJO.Curso;
import POJO.Usuario;
import Utils.ShowMessage;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class MenuEstudianteController implements Initializable {

    @FXML
    private Label lbMisCursos;
    @FXML
    private GridPane gridCursos;
    
    Usuario usuario = UsuarioDAO.getUsuarioActual();
    ArrayList<Curso> listaCursos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            listaCursos = CursoDAO.obtenerCursosEstudiante(usuario);
        } catch (Exception e) {
            ShowMessage
                    .showAlertSimple("ERROR",
                            "No se pueden cargar los cursos",
                            Alert.AlertType.ERROR);
        }
        
        cargarCursos();
    }

    private void cargarCursos(){
        int column = 0;
        int row = 1;
        
            for (int i = 0; i < this.listaCursos.size(); i++) {
                CursoEstudianteItemController.cursoActual = listaCursos.get(i);
                CursoEstudianteItemController.estudianteActual = usuario;
            
                FXMLLoader fxmlloader = new FXMLLoader(getClass()
                        .getResource(("/Views/CursoEstudianteItem.fxml")));
                AnchorPane anchorPane;
                
                try {
                    anchorPane = fxmlloader.load();
                    CursoEstudianteItemController item = fxmlloader.getController();
                    item.ponerDatos(listaCursos.get(i));
                    
                    if (column == 1) {
                        column = 0;
                        row ++;
                    }
                    
                    gridCursos.add(anchorPane, column++, row);
                    gridCursos.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridCursos.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    gridCursos.setMaxWidth(Region.USE_PREF_SIZE);
                    gridCursos.setMinHeight(Region.USE_COMPUTED_SIZE);
                    gridCursos.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridCursos.setMaxHeight(Region.USE_PREF_SIZE);
                    GridPane.setMargin(anchorPane, new Insets(10));
                } catch (Exception e) {
                    ShowMessage
                        .showAlertSimple("ERROR",
                                "No se puede mostrar la ventana"
                                , Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
                
            }
    }

    @FXML
    private void clicCerrarSesion(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/FXMLLogin.fxml"));
            Parent root = loader.load();
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage)this.gridCursos.getScene().getWindow();
            currentStage.close();
        }catch(IOException ioe){
            ShowMessage.showAlertSimple(
                "Error", 
                "Error al sesión", 
                Alert.AlertType.ERROR
            );
        }
    }   
}
