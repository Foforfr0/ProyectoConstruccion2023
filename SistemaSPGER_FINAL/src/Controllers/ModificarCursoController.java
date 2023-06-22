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
import java.io.IOException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ModificarCursoController implements Initializable {

    @FXML
    private ComboBox cbExperienciaEducativa = new ComboBox();
    @FXML
    private TextField tfPeriodo;
    @FXML
    private Button idBtVolver;
    @FXML
    private Button idBtCrearCurso;
    @FXML
    private Label lbPeriodoValido;
    @FXML
    private TextField tfNombreCurso;
    @FXML
    private Label lbNombreVacio;
    
    private ObservableList<ExperienciaEducativa> listaExperiencias;
    private Curso curso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaRolesSistema();
        lbPeriodoValido.setVisible(false);
        lbNombreVacio.setVisible(false);
        cbExperienciaEducativa.getSelectionModel().selectFirst();
    }    

    @FXML
    private void btVolver(ActionEvent event) {
        cerrarVentana();
        abrirPrincipal();
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) tfNombreCurso.getScene().getWindow();
        escenario.close();
    }
    
    private void abrirPrincipal(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/PrincipalAcademico.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    private int obtenerPosicionEE(int idEE){
        for (int i = 0; i < listaExperiencias.size(); i++) {
            if (listaExperiencias.get(i).getIdExperienciaEducativa() == idEE) {
                return i;
            }
        }
        return 0;
    }

    @FXML
    private void btCrearCurso(ActionEvent event) {
        String periodo = tfPeriodo.getText();
        ExperienciaEducativa EE = (ExperienciaEducativa)cbExperienciaEducativa.getValue();
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
                Curso cursoEditado = new Curso();
                    cursoEditado.setIdcurso(this.curso.getIdcurso());
                    cursoEditado.setPeriodo(periodo);
                    cursoEditado.setNombre(nombre);
                    cursoEditado.setExperienciaEducativa(experienciaEducativa);
                    cursoEditado.setIdProfesor(academicoActual.getIdUsuario());
                modificarCurso(cursoEditado);
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
    
    private void modificarCurso(Curso curso) throws SQLException{
        try {
            ResultadoOperacion resultado = CursoDAO.modificarCurso(curso);
            System.out.println(curso.getNombre());
            System.out.println(curso.getPeriodo());
            System.out.println(curso.getExperienciaEducativa());
            System.out.println(curso.getIdProfesor());
            System.out.println(curso.getIdcurso());
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Modificar curso",
                        resultado.getMensaje(),
                        Alert.AlertType.INFORMATION);
            } else {
                ShowMessage.showAlertSimple("Modificar curso",
                        resultado.getMensaje(),
                        Alert.AlertType.INFORMATION);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setDatos(Curso curso){
        this.curso = curso;
        tfNombreCurso.setText(curso.getNombre());
        tfPeriodo.setText(curso.getPeriodo());
        int posicionCB = obtenerPosicionEE(curso.getExperienciaEducativa());
        cbExperienciaEducativa.getSelectionModel().select(posicionCB);
    }
}
