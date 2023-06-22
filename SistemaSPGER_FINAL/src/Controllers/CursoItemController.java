package Controllers;

import DAO.AvancesDAO;
import POJO.Curso;
import Utils.ShowMessage;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class CursoItemController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private Button btnAvances;
    
    private Curso curso;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void ponerDatos(Curso curso){
        lbNombre.setText(curso.getNombre());
        this.curso = curso;
        System.out.println("ID del curso que se paso al item: " + curso.getIdcurso());
    }

    @FXML
    private void btnActividades(ActionEvent event) {
        irActividadesCurso();
        cerrarVentana();
    }
    
    private void irActividadesCurso(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/ActividadesDelCurso.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
            
            ActividadesDelCursoController controlador = loader.getController();
            controlador.ponerDatos(curso);
            //controlador.cargarActividades();
            controlador.cargarLista(curso);
            
            cerrarVentana();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnAvances(ActionEvent event) {
        irAvancesCurso();
        cerrarVentana();
    }
    
    private void irAvancesCurso(){
         try {
            AvancesDAO.obtenerAvances(this.curso);
            AvancesDAO.setCursoActual(curso);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConsultarAvancesProfesor.fxml"));
            ConsultarAvancesProfesorController controlador = loader.getController();
            controlador.setCursoActual(this.curso);
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
            
            cerrarVentana();
        } catch(SQLException sql){
            ShowMessage.showAlertSimple(
                "Error",
                "No se puede conectar a la base de datos",
                Alert.AlertType.ERROR
            );
            sql.printStackTrace();
        }catch (Exception e) {
            ShowMessage.showAlertSimple(
                "Error",
                "No se puede mostrar la ventana",
                Alert.AlertType.ERROR
            );
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana(){
        Stage stageActual = (Stage)lbNombre.getScene().getWindow();
        stageActual.close();
    }

    private void btnID(ActionEvent event) {
        System.out.println(curso.getIdcurso());
    }

    @FXML
    private void btnModificarCurso(ActionEvent event) {
        irModificarCurso();
        PrincipalAcademicoController controlador = new PrincipalAcademicoController();
    }
    
    private void irModificarCurso(){
        try {
            FXMLLoader accesoControlador = new FXMLLoader(getClass()
                .getResource("/Views/ModificarCurso.fxml"));
            Parent vista = accesoControlador.load();
            ModificarCursoController formularioCurso = accesoControlador
                    .getController();
            formularioCurso.setDatos(this.curso);
            
            //Obtener la escena y el escenario de la ventana actual
            Scene escenaActual = lbNombre.getScene();
            Stage escenarioActual = (Stage) escenaActual.getWindow();
            
            escenarioActual.close();
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene(vista));
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
