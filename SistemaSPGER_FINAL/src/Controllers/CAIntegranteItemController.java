/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import POJO.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class CAIntegranteItemController implements Initializable {

    @FXML
    private Label lbNombres;
    @FXML
    private Label lbApellidoPaterno;
    @FXML
    private Label lbApellidoMaterno;
    
    private Usuario usuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void ponerDatos(Usuario usuario){
        lbNombres.setText(usuario.getNombre());
        lbApellidoPaterno.setText(usuario.getApellidoPaterno());
        lbApellidoMaterno.setText(usuario.getApellidoPaterno());
        this.usuario = usuario;
    }
}
