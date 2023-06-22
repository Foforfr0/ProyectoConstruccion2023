/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.UsuarioDAO;
import POJO.Anteproyecto;
import POJO.Usuario;
import Utils.ShowMessage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class EstudianteAnteproyectoItemController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private Usuario Usuario;
    private Anteproyecto anteproyecto;
    @FXML
    private Label lbNombreEstudiante;
    @FXML
    private Button idBotonEliminar;
    @FXML
    private Label lbMatricula;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void ponerDatos(Usuario usuario, Anteproyecto anteproyectoSeleccionado){
        this.Usuario = usuario;
        lbMatricula.setText("Matricula: " + usuario.getMatricula());
        lbNombreEstudiante.setText(usuario.toString());
        this.anteproyecto=anteproyectoSeleccionado;
    }

    @FXML
    private void botonEliminar(ActionEvent event) {
        try {
            ResultadoOperacion resultado = UsuarioDAO.eliminarEstudianteAnteproyecto(Usuario);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Estudiante eliminado",
                        "Estudiante quitado del ateproyecto",
                        Alert.AlertType.INFORMATION);
            } else {
                ShowMessage.showAlertSimple("ERROR",
                        "No se eliminó el usuario",
                        Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
