/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import DAO.AcademicoCuerpoAcademicoDAO;
import POJO.CuerpoAcademico;
import POJO.GradoConsolidacion;
import POJO.Usuario;
import Utils.ShowMessage;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class FormularioCA_ConsultaController implements Initializable {
    private ObservableList<GradoConsolidacion> listaGradosConsolidacion;
    @FXML
    private ComboBox cbGradoConsodilacion = new ComboBox();
    @FXML
    private TextField tfClave;
    @FXML
    private DatePicker dpFechaRegistro;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfDesAdscripcion;
    @FXML
    private TableView tableUsuarios;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    
    @FXML
    private ChoiceBox chbAcademicoResponsable = new ChoiceBox();
    @FXML
    private Button btnCancelarRegistro;
    
    ObservableList<Usuario> listaUsuariosResponsables;
    ObservableList<Usuario> listaUsuariosIntegrantes;
    
    @FXML
    private Label lbTituloFormulario;
    @FXML
    private Button btnRegistrarCA;
    
    private CuerpoAcademico cuerpoAcademico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void iniciarConsulta() {
        // TODO
        configurarTabla();
        cargarDatosCA();
        cargarListaGradoConsolidacion(cuerpoAcademico.getGradoConsodilacion());
        cargarDatosAcademicoResponsable(cuerpoAcademico.getIdcuerpoAcademico());
        cargarDatosAcademicosIntegrantes(cuerpoAcademico.getIdcuerpoAcademico());
    }
    
    private void cargarListaGradoConsolidacion(String gradoConsolidacion){
        listaGradosConsolidacion = FXCollections.observableArrayList();
        try {
            GradoConsolidacion gc1 = new GradoConsolidacion(1,"Cuerpo académico en formación");
            GradoConsolidacion gc2 = new GradoConsolidacion(2,"Cuerpo académico en consolidación");
            GradoConsolidacion gc3 = new GradoConsolidacion(3,"Cuerpo académico consolidado");
            listaGradosConsolidacion.add(gc1);
            listaGradosConsolidacion.add(gc2);
            listaGradosConsolidacion.add(gc3);
            cbGradoConsodilacion.setItems(listaGradosConsolidacion);
            cbGradoConsodilacion.setValue(gradoConsolidacion);
            cbGradoConsodilacion.setDisable(true);
            cbGradoConsodilacion.setStyle("-fx-opacity: 1");
            cbGradoConsodilacion.getEditor().setStyle("-fx-opacity: 1");
            //cbGradoConsodilacion.editableProperty().set(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void configurarTabla(){
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
            colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
    }
    
    private void cargarDatosAcademicoResponsable(int cuerpoacademicoId){
        try {
            listaUsuariosResponsables = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioResponsable = AcademicoCuerpoAcademicoDAO.obtenerAcademicosCuerpoAcademicoResponsable(cuerpoacademicoId);
            listaUsuariosResponsables.addAll(usuarioResponsable);
            chbAcademicoResponsable.setItems(listaUsuariosResponsables);
            chbAcademicoResponsable.setValue(listaUsuariosResponsables.get(0));
            chbAcademicoResponsable.setDisable(true);
            chbAcademicoResponsable.setStyle("-fx-opacity: 1");
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta", Alert.AlertType.ERROR);
        }catch (NullPointerException n){
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDatosAcademicosIntegrantes(int cuerpoacademicoId){
        try {
            listaUsuariosIntegrantes = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioResponsable = AcademicoCuerpoAcademicoDAO.obtenerAcademicosCuerpoAcademicoIntegrante(cuerpoacademicoId);
            listaUsuariosIntegrantes.addAll(usuarioResponsable);
            tableUsuarios.setItems(listaUsuariosIntegrantes);
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta", Alert.AlertType.ERROR);
        }catch (NullPointerException n){
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDatosCA(){
        tfClave.setText(cuerpoAcademico.getClave());
        dpFechaRegistro.setValue(cuerpoAcademico.getFechaRegistro().toLocalDate());
        dpFechaRegistro.setDisable(true);
        dpFechaRegistro.setStyle("-fx-opacity: 1");
        dpFechaRegistro.getEditor().setStyle("-fx-opacity: 1");

        tfNombre.setText(cuerpoAcademico.getNombre());
        tfDesAdscripcion.setText(cuerpoAcademico.getDesAdscripcion());
    }
    
    @FXML
    private void btnCancelarConsulta(ActionEvent event) {
        try {
                cerrarVentana();
                abrirGestion();
        } catch (Exception e) {   
        }
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) tfNombre.getScene().getWindow();
        escenario.close();
    }
    
    private void abrirGestion(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/GestionCA.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void asignarCuerpoAcademicoEdicion(CuerpoAcademico cuerpoAcademico){
        this.cuerpoAcademico = cuerpoAcademico;
        if (cuerpoAcademico != null) {
            iniciarConsulta();
        }
    }
    
}
