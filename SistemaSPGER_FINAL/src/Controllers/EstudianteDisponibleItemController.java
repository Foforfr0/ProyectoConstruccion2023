package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.UsuarioDAO;
import POJO.Anteproyecto;
import POJO.Usuario;
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
public class EstudianteDisponibleItemController implements Initializable {

    @FXML
    private Button idBtnAgregar;
    
    private Anteproyecto anteproyecto;
    private Usuario Usuario;
    
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbMatricula;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnAgregar(ActionEvent event) throws SQLException {
        ResultadoOperacion resultado = UsuarioDAO.asignarAnteproyecto(this.Usuario,
                this.anteproyecto);
        if(!resultado.isError()){
            Utils.ShowMessage.showAlertSimple("Anteproyecto asignado",
                    "El anteproyecto "+anteproyecto.getNombre()+
                    "fue asignado al estudiante "+Usuario.getNombre(),
                    Alert.AlertType.INFORMATION);
        }else{
            Utils.ShowMessage.showAlertSimple("ERROR",
                    "Error al asignar anteproyecto", Alert.AlertType.ERROR);
        }
    }
    
    public void ponerDatos(Usuario usuario,Anteproyecto anteproyecto){
        this.Usuario=usuario;
        lbNombre.setText(usuario.toString());
        lbMatricula.setText("Matricula: " + usuario.getMatricula());
        this.anteproyecto=anteproyecto;
        
    }
    
    
}
