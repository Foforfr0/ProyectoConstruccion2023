package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import Utils.ShowMessage;
import java.io.IOException;
import javafx.scene.input.MouseEvent;


public class PrincipalAdministradorController implements Initializable {

    @FXML
    private Button btnGestionUsuariosVentana;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnGestionUsuarios(ActionEvent event) {
        try {
            //abrir la ventana
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/GestionUsuarios.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            //cerrar la ventana actual
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();
    } catch (Exception ex) {
        ShowMessage.showAlertSimple("Error", 
                "No se puede mostrar la ventana", Alert.AlertType.ERROR);
    }
    }

    @FXML
    private void btnGestionCA(ActionEvent event) {
        try {
            //abrir venatana gestionCA
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/GestionCA.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();;
            //Cerrar la ventana actual
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();
        } catch (Exception e) {
            ShowMessage
                    .showAlertSimple("Error",
                            "No se puede mostrar la ventana",
                            Alert.AlertType.ERROR);
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
            Stage currentStage = (Stage)this.btnGestionUsuariosVentana.getScene().getWindow();
            currentStage.close();
        }catch(IOException ioe){
            ShowMessage.showAlertSimple(
                "Error", 
                "Error al sesión", 
                Alert.AlertType.ERROR
            );
        }
    }
}
