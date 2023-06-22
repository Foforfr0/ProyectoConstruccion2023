/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.CursoDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import DAO.UsuarioDAO;
import POJO.Curso;
import POJO.Usuario;
import Utils.ShowMessage;
import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author lecap
 */
public class PrincipalAcademicoController implements Initializable {
    
    Usuario usuarioActual = UsuarioDAO.getUsuarioActual();
    @FXML
    private Button btnAnteproyectos;
    int idAcademicoSesion = UsuarioDAO.getUsuarioActual().getIdUsuario();
    @FXML
    private Label lbMisCursos;
    
    private ArrayList<Curso> listaCursos;
    @FXML
    private GridPane gridCursos;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            listaCursos = CursoDAO.obtenerCursos(idAcademicoSesion);
        } catch (Exception e) {
            ShowMessage
                    .showAlertSimple("ERROR",
                            "No se pueden cargar los cursos",
                            Alert.AlertType.ERROR);
        }
        cargarCursos();
    } 

    @FXML
    private void btnPostularAnteproyecto(ActionEvent event) {
        try {
            //abrir la ventana
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/CrearAnteproyecto.fxml"));
            if (loader != null){
                System.out.println("Se carga con exito el principal");
            } else {
                System.out.println("No ee carga con exito el prinicpal");
            }
            Parent root = loader.load();
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
    
    @FXML
    private void btnConsultarMisAnteproyectos(ActionEvent event) {
        irMisAnteproyectos();
    }
    
    private void irMisAnteproyectos(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/ConsultarAnteproyectos.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage escenarioBase = (Stage) lbMisCursos.getScene().getWindow();
        escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btCrearCurso(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/CrearCurso.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage escenarioBase = (Stage) lbMisCursos.getScene().getWindow();
        escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    
    public void cargarCursos(){
        if(!this.listaCursos.isEmpty()){
        };
        int column = 0;
        int row = 1;
        
        for (int i = 0; i < this.listaCursos.size(); i++) {
            FXMLLoader fxmloader = new FXMLLoader(getClass()
                    .getResource("/Views/CursoItem.fxml"));
            AnchorPane anchorPane;
            
            try {
                anchorPane = fxmloader.load();
                CursoItemController itemController = fxmloader
                        .getController();
                itemController.ponerDatos(listaCursos.get(i));
                
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
            } catch (IOException e) {
                ShowMessage
                        .showAlertSimple("ERROR",
                                "No se puede mostrar la ventana"
                                , Alert.AlertType.ERROR);
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
            Stage currentStage = (Stage)this.lbMisCursos.getScene().getWindow();
            currentStage.close();
        }catch(IOException ioe){
            ShowMessage.showAlertSimple(
                "Error", 
                "Error al sesión", 
                Alert.AlertType.ERROR
            );
        }
    }
    
    public void cerrarVentana(){
        Stage stageActual = (Stage) lbMisCursos.getScene().getWindow();
        stageActual.close();
    }
}
