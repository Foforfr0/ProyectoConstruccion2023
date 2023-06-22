package Controllers;

import POJO.CuerpoAcademico;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class CAItemController implements Initializable {

    @FXML
    private Label lbNombreCA;
    @FXML
    private Label lbFechaRegistro;
    @FXML
    private Label lbClave;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setData(CuerpoAcademico ca){
        lbNombreCA.setText(ca.getNombre());
    }
    
}
