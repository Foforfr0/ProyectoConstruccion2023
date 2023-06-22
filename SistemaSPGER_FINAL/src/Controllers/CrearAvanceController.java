package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class CrearAvanceController implements Initializable {

    @FXML
    private Button idBtnCancelar;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaCierre;
    @FXML
    private Pane panel;
    @FXML
    private TextField tfNumAvance;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.validarNumAvance();
    }        

    @FXML
    private void btnCancelar(ActionEvent event) {
    }

    @FXML
    private void btnCrearActividad(ActionEvent event) {
    }

    @FXML
    private void btnAdjuntarArchivo(ActionEvent event) {
    }

    @FXML
    private void btnID(ActionEvent event) {
    }

    @FXML
    private void validarNumAvance(){    
        // Crear el filtro para aceptar solo números
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        });
        
        this.tfNumAvance.setTextFormatter(formatter);
    }
}
