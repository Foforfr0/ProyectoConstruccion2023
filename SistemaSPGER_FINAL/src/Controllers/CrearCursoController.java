/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.CursoDAO;
import DAO.ExperienciaEducativaDAO;
import DAO.UsuarioDAO;
import POJO.Curso;
import POJO.ExperienciaEducativa;
import POJO.Usuario;
import Utils.ShowMessage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class CrearCursoController implements Initializable {

    @FXML
    private ComboBox cbExperienciaEducativa = new ComboBox();
    @FXML
    private TextField tfPeriodo;
    @FXML
    private Button idBtVolver;
    @FXML
    private Button idBtCrearCurso;
    
    private ObservableList<ExperienciaEducativa> listaExperiencias;
    @FXML
    private Label lbPeriodoValido;
    @FXML
    private Label lbEESeleccionada;
    @FXML
    private TextField tfNombreCurso;
    @FXML
    private Label lbNombreVacio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaRolesSistema();
        lbPeriodoValido.setVisible(false);
        lbEESeleccionada.setVisible(false);
        lbNombreVacio.setVisible(false);
        cbExperienciaEducativa.getSelectionModel().selectFirst();
    }    

    @FXML
    private void btVolver(ActionEvent event) {
        volverPrincipalAcademico();
    }
    
    private void volverPrincipalAcademico(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Views/PrincipalAcademico.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage escenarioBase = (Stage) idBtVolver.getScene().getWindow();
        escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btCrearCurso(ActionEvent event) {
        String periodo = tfPeriodo.getText();
        ExperienciaEducativa EE = (ExperienciaEducativa)cbExperienciaEducativa
                .getValue();
        int experienciaEducativa = EE.getIdExperienciaEducativa();
        String nombre = tfNombreCurso.getText();
        Usuario academicoActual = UsuarioDAO.getUsuarioActual();
        if (!validarPeriodo(periodo)) {
            tfPeriodo.setStyle("-fx-border-color: red;");
            lbPeriodoValido.setVisible(true);
        } else if(nombre.isEmpty()) {
            lbNombreVacio.setVisible(true);
        } else {
            try {
                Curso nuevoCurso = new Curso();
                nuevoCurso.setPeriodo(periodo);
                nuevoCurso.setNombre(nombre);
                nuevoCurso.setExperienciaEducativa(experienciaEducativa);
                nuevoCurso.setIdProfesor(academicoActual.getIdUsuario());
                crearCruso(nuevoCurso);
                volverPrincipalAcademico();
            } catch (SQLException e) {
               ShowMessage.showAlertSimple("Error",
                    "Error de conexión",
                    Alert.AlertType.ERROR); 
            }
        }
    }
    
    private void cargarListaRolesSistema(){
        listaExperiencias = FXCollections.observableArrayList();
        try {
            ArrayList<ExperienciaEducativa> rolSistemaDB = ExperienciaEducativaDAO
                    .obtenerExperienciasEducativas();
            listaExperiencias.addAll(rolSistemaDB);
            cbExperienciaEducativa.setItems(listaExperiencias);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean validarPeriodo(String periodo){
        return periodo.matches("^\\d{4}-(A|B)$");
    }
    
    private void crearCruso(Curso curso) throws SQLException{
        try {
            ResultadoOperacion resultadoCrearCurso = CursoDAO
                    .crearCurso(curso);
            if (!resultadoCrearCurso.isError()) {
                ShowMessage.showAlertSimple("Mensaje",
                        "Curso creado",
                        Alert.AlertType.INFORMATION);
            } else {
               ShowMessage.showAlertSimple("ERROR",
                        "No se pudo crear el curso",
                        Alert.AlertType.ERROR); 
            }
        } catch (SQLException e) {
            ShowMessage.showAlertSimple("Error",
                    "Error de conexión",
                    Alert.AlertType.ERROR);
        }
    }
}
