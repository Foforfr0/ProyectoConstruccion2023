package Controllers;

import static Controllers.ConsultarAvancesProfesorController.cursoActual;
import POJO.Curso;
import POJO.Usuario;
import Utils.ShowMessage;
import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CursoEstudianteItemController implements Initializable {

    @FXML
    private Label lbNombreCurso;
    @FXML
    private Label lbPeriodo;
    
    public static Usuario estudianteActual = null;
    public static Curso cursoActual = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void btnConsultarActividades(ActionEvent event) {
    }

    @FXML
    private void btnConsultarAvances(ActionEvent event) throws IOException { 
        
            ConsultarAvancesEstudianteController.cursoActual = cursoActual;
            ConsultarAvancesEstudianteController.estudianteActual = estudianteActual;
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConsultarAvancesEstudiante.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage)this.lbNombreCurso.getScene().getWindow();
            currentStage.close();
        
    }
    
    public void ponerDatos(Curso curso){
        lbNombreCurso.setText(curso.getNombre());
        lbPeriodo.setText("Periodo: " + curso.getPeriodo());
    }   
}
