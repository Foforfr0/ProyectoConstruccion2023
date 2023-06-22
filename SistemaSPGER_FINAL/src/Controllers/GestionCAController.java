package Controllers;

import DAO.CuerpoAcademicoDAO;
import POJO.CuerpoAcademico;
import Utils.ShowMessage;
import javafx.geometry.Insets;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class GestionCAController implements Initializable {

    private GridPane gridCA;
    private ArrayList<CuerpoAcademico> cuerposAcademicos;
    @FXML
    private Button btnCrearCAid;
    @FXML
    private Button btnConsultarCAid;
    @FXML
    private Button btnModificarCAid;
    @FXML
    private TableView tableCuerpoAcademico;
    @FXML
    private TableColumn colIDcuerpoAcademico;
    @FXML
    private TableColumn colClave;
    @FXML
    private TableColumn colGradoConsodilacion;
    @FXML
    private TableColumn colFechaRegistro;
    @FXML
    private TableColumn colDesAdscripcion;
    @FXML
    private TableColumn colNombre;
    
    
    ObservableList<CuerpoAcademico> listaCuerposAcademicos;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicializando ventana GestionCAController");
        configurarTabla();
        try {
            cuerposAcademicos=CuerpoAcademicoDAO.obtenerCuerposAcademicos();
            System.err.println(cuerposAcademicos.size()+"ANTEPROYEDFOTERER");
        } catch (SQLException ex) {
            System.err.println("ERROR AL CARGAR CUERPOS ACADEMICOS");
        }
        cargarCuerposAcademicos();
    }    

    @FXML
    private void btnCrearCA(ActionEvent event) {
        //btnCrearCAid
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/FormularioCA.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            cerrarVentana();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarCuerposAcademicosOLD() {
        if(this.cuerposAcademicos.size()>0){
            };
        int column=0;
        int row=1;
        for(int i=0;i<this.cuerposAcademicos.size();i++){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/CAItem.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = fxmlLoader.load();
                CAItemController itemController = fxmlLoader.getController();
                itemController.setData(cuerposAcademicos.get(i));
                        
            if(column==1){
                column = 0;
                row++;
            }
            gridCA.add(anchorPane, column++, row);
            gridCA.setMinWidth(Region.USE_COMPUTED_SIZE);
            gridCA.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gridCA.setMaxWidth(Region.USE_PREF_SIZE);
            gridCA.setMinHeight(Region.USE_COMPUTED_SIZE);
            gridCA.setPrefHeight(Region.USE_COMPUTED_SIZE);
            gridCA.setMaxHeight(Region.USE_PREF_SIZE);
            GridPane.setMargin(anchorPane, new Insets(10));
        } catch (IOException ex) {
                Logger.getLogger(GestionCAController.class.getName()).log(Level.SEVERE, null, ex);                
            }
        }
    }
    
    private void cargarCuerposAcademicos(){
        try {
            listaCuerposAcademicos = FXCollections.observableArrayList();
            ArrayList<CuerpoAcademico> cuerpoAcademicoBD = CuerpoAcademicoDAO.obtenerCuerposAcademicos();
            if(cuerpoAcademicoBD.isEmpty()){
                btnConsultarCAid.setVisible(false);
                btnModificarCAid.setVisible(false);
            }else{
                btnConsultarCAid.setVisible(true);
                btnModificarCAid.setVisible(true);
            }
            listaCuerposAcademicos.addAll(cuerpoAcademicoBD);
            tableCuerpoAcademico.setItems(listaCuerposAcademicos);
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta"
                    , Alert.AlertType.ERROR);
        }catch (NullPointerException n){
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio"
                    , Alert.AlertType.ERROR);
        }
    }
    

    private void ivVolverClic(MouseEvent event) {
        cerrarVentana();
        irPrincipalAdministrador();
    }
    
    private void cerrarVentana(){
        Stage x = (Stage) btnCrearCAid.getScene().getWindow();
        x.close();
    }
    
    private void irPrincipalAdministrador(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PrincipalAdministrador.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void btnVolver(ActionEvent event) throws IOException{
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("/Views/PrincipalAdministrador.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        cerrarVentana();
    }
    
    private void configurarTabla(){
            colIDcuerpoAcademico.setCellValueFactory(new PropertyValueFactory<>("idcuerpoAcademico"));
            colClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
            colGradoConsodilacion.setCellValueFactory(new PropertyValueFactory<>("gradoConsodilacion"));
            colFechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
            colDesAdscripcion.setCellValueFactory(new PropertyValueFactory<>("desAdscripcion"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    }
    
    @FXML
    private void btnModificarCA(ActionEvent event) {
        try {
            int filaSellecionada = tableCuerpoAcademico.getSelectionModel().getSelectedIndex();
            CuerpoAcademico ca = (filaSellecionada >= 0 ) ? listaCuerposAcademicos.get(filaSellecionada) : null;
            
            if (ca != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/FormularioCA_Modificacion.fxml"));
                Parent root = loader.load();
                
                FormularioCA_ModificacionController modificarCAController = loader.getController();
                modificarCAController.asignarCuerpoAcademicoEdicion(ca);
                
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                cerrarVentana();
            }else{
                ShowMessage.showAlertSimple("Seleccione un Cuerpo Academico", 
                    "Debe seleccionar un Cuerpo Academico", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnConsultarCA(ActionEvent event) {
        try {
            int filaSellecionada = tableCuerpoAcademico.getSelectionModel().getSelectedIndex();
            CuerpoAcademico ca = (filaSellecionada >= 0 ) ? listaCuerposAcademicos.get(filaSellecionada) : null;
            
            if (ca != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/FormularioCA_Consulta.fxml"));
                Parent root = loader.load();
                
                FormularioCA_ConsultaController consultarCAController = loader.getController();
                consultarCAController.asignarCuerpoAcademicoEdicion(ca);
                
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                cerrarVentana();
            }else{
                ShowMessage.showAlertSimple("Seleccione un Cuerpo Academico", 
                    "Debe seleccionar un Cuerpo Academico", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnPrueba(ActionEvent event) {
        try {
            int filaSeleccionada = tableCuerpoAcademico.getSelectionModel().getSelectedIndex();
            CuerpoAcademico ca = (filaSeleccionada >= 0) ? listaCuerposAcademicos.get(filaSeleccionada) : null;
            
            if (ca != null) {
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/Views/ConsultarCA.fxml"));
                Parent root = loader.load();
                
                ConsultarCAController controller = loader.getController();
                controller.setIdCuerpoAcademico(ca.getIdcuerpoAcademico());
                
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                cerrarVentana();
                System.out.println("ID del CA seleccionado: " + ca.getIdcuerpoAcademico());
            } else {
                ShowMessage.showAlertSimple("Seleccione un Cuerpo Academico", 
                    "Debe seleccionar un Cuerpo Academico", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
