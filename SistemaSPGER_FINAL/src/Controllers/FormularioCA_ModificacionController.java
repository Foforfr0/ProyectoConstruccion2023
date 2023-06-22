/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.AcademicoCuerpoAcademicoDAO;
import DAO.CuerpoAcademicoDAO;
import DAO.UsuarioDAO;
import POJO.CuerpoAcademico;
import POJO.GradoConsolidacion;
import POJO.Usuario;
import Utils.ShowMessage;
import exception.SpgerException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class FormularioCA_ModificacionController implements Initializable {
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
    
    ObservableList<Usuario> listaUsuariosResponsables;
    ObservableList<Usuario> listaUsuariosIntegrantes;
    ObservableList<Usuario> listaUsuarios;
    
    
    @FXML
    private Label lbTituloFormulario;
    
    private CuerpoAcademico cuerpoAcademico;
    @FXML
    private Button btnCancelarConsulta;
    @FXML
    private Button btnGuardarCambiosId;

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
        //cargarDatosAcademicos();
        cargarDatosAcademicos2();
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
            //cbGradoConsodilacion.editableProperty().set(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void configurarTabla(){
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
            colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
            
            //Habilitar la seleccion multiple sin la tecla CTRL
            
    }
    
    private void cargarDatosAcademicos(){
        try {
            listaUsuarios = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioBD = UsuarioDAO.obtenerUsuariosAcademicos();
            listaUsuarios.addAll(usuarioBD);
            tableUsuarios.setItems(listaUsuarios);
            tableUsuarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            chbAcademicoResponsable.setItems(listaUsuarios);
            for(Usuario academico : listaUsuarios) {
                if(academico.getIdUsuario() == listaUsuariosResponsables.get(0).getIdUsuario()){
                    chbAcademicoResponsable.setValue(listaUsuarios.get(0));
                }
            }
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta", Alert.AlertType.ERROR);
        }catch (NullPointerException n){
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDatosAcademicos2(){
        try {
            listaUsuarios = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioBD = UsuarioDAO.obtenerUsuariosAcademicos();
            listaUsuarios.addAll(usuarioBD);
            tableUsuarios.setItems(listaUsuarios);
            tableUsuarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            
            for (Usuario integrante :listaUsuariosIntegrantes) {
                for (Usuario academico : listaUsuarios) {
                    if (academico.getIdUsuario() == integrante.getIdUsuario()) {
                        tableUsuarios.getSelectionModel().select(academico);
                        break;
                    }
                }
            }
            
            chbAcademicoResponsable.setItems(listaUsuarios);
            for(Usuario academico : listaUsuarios){
                if (academico.getIdUsuario() == listaUsuariosResponsables.get(0).getIdUsuario()) {
                    chbAcademicoResponsable.setValue(academico);
                }
            }
        } catch (SQLException e) {
            ShowMessage.showAlertSimple("Error de SQL",
                    "Error en la consulta", Alert.AlertType.ERROR);
        } catch (NullPointerException nu) {
            ShowMessage.showAlertSimple("Error de puntero",
                    "Objeto vacio", Alert.AlertType.ERROR);
        }
    }
    
    private void cargarDatosAcademicoResponsable(int cuerpoacademicoId){
        try {
            listaUsuariosResponsables = FXCollections.observableArrayList();
            ArrayList<Usuario> usuarioResponsable = AcademicoCuerpoAcademicoDAO.obtenerAcademicosCuerpoAcademicoResponsable(cuerpoacademicoId);
            listaUsuariosResponsables.addAll(usuarioResponsable);
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

        tfNombre.setText(cuerpoAcademico.getNombre());
        tfDesAdscripcion.setText(cuerpoAcademico.getDesAdscripcion());
    }
    
    @FXML
    private void btnCancelarConsulta(ActionEvent event) {
        try {
            ShowMessage.showAlertSimple("Descartar Cambios",
                    "Cambios Descartados", Alert.AlertType.INFORMATION);
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
    
    @FXML
    private void btnGuardarCambios(ActionEvent event) {
        String desAdscripcion = null;
        String nombre = null;
        String clave = null;
        try {
            //Obtener valores para CA
            if (tfClave.getText() != null && !tfClave.getText().trim().isEmpty()) clave = tfClave.getText();
            String gradoConsodilacion = cbGradoConsodilacion.getValue().toString();
            //ZoneId z = ZoneId.of( "America/Mexico_City" );
            //Date fechaRegistro = Date.from(dpFechaRegistro.getValue().atStartOfDay(z).toInstant());
            Date fechaRegistro = Date.valueOf(dpFechaRegistro.getValue());
            if (tfDesAdscripcion.getText() != null && !tfDesAdscripcion.getText().trim().isEmpty()) desAdscripcion = tfDesAdscripcion.getText();
            if (tfNombre.getText() != null && !tfNombre.getText().trim().isEmpty()) nombre = tfNombre.getText();
            
            CuerpoAcademico cuerpoAcademicoActualizado = new CuerpoAcademico();
            cuerpoAcademicoActualizado.setIdcuerpoAcademico(cuerpoAcademico.getIdcuerpoAcademico());
            cuerpoAcademicoActualizado.setClave(clave);
            cuerpoAcademicoActualizado.setGradoConsodilacion(gradoConsodilacion);
            cuerpoAcademicoActualizado.setFechaRegistro(fechaRegistro);
            cuerpoAcademicoActualizado.setDesAdscripcion(desAdscripcion.trim());
            cuerpoAcademicoActualizado.setNombre(nombre.trim());
            
            //leer Academico Responsable y los intengranted de la nueva CA
            Map<Integer,String> academicos = new HashMap<Integer,String>();
            Usuario academicoResponsable = (Usuario)(chbAcademicoResponsable.getSelectionModel().getSelectedItem());
            if(academicoResponsable == null || academicoResponsable.getIdUsuario()<=0) throw new SpgerException(
                    "Por favor seleccione un Responsable del Cuerpo Academico");
            academicos.put(academicoResponsable.getIdUsuario(), "Responsable");
            ObservableList<Usuario> integrantesSeleccionados = FXCollections.observableArrayList();
            integrantesSeleccionados = tableUsuarios.getSelectionModel().getSelectedItems();
            
            for(Usuario academicoIntegrante : integrantesSeleccionados) {
                if(academicoIntegrante.getIdUsuario() == academicoResponsable.getIdUsuario()){
                    throw new SpgerException("El Responsable del Cuerpo Academico no debe estar seleccionado como Integrante");
                }
            }
            
            for(Usuario academicoIntegrante : integrantesSeleccionados) {
                academicos.put(academicoIntegrante.getIdUsuario(), "Integrante");
            }
            
            //Validar usuarios seleccionados
            int responsablesCount = 0;
            int integrantesCount = 0;
            for(int key : academicos.keySet()) {
                if(academicos.get(key).equals("Responsable")) {
                responsablesCount++;
                }else{
                    integrantesCount++;
                }
            }
            if(responsablesCount <= 0) throw new SpgerException("Por favor seleccione un Responsable del Cuerpo Academico");
            if(integrantesCount <= 0) throw new SpgerException("Por favor seleccione al menos un Integrante del Cuerpo Academico");
    
            actualizarCA(cuerpoAcademicoActualizado, academicos);
        } catch (NullPointerException e) {
            ShowMessage.showAlertSimple("Campos faltantes",
                    "Faltan campos en el formulario", Alert.AlertType.WARNING);
        }catch(SpgerException e){
            ShowMessage.showAlertSimple("Campos faltantes",
                    e.getMessage(), Alert.AlertType.WARNING);
        }
    }
    
    private void actualizarCA(CuerpoAcademico cuerpoAcademico, Map<Integer,String> usuarios){
        try {
            ResultadoOperacion resultado = CuerpoAcademicoDAO.actualizarCA(cuerpoAcademico, usuarios);
            if (!resultado.isError()) {
                try {
                    ShowMessage.showAlertSimple("Registro", "Cuerpo Academico registrado"
                        , Alert.AlertType.INFORMATION);
                    cerrarVentana();
                    abrirGestion();
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }else{
                ShowMessage.showAlertSimple("ERROR", "Campos faltantes (CA)",
                        Alert.AlertType.WARNING);          
            }
        } catch (SQLException ex){
            ShowMessage.showAlertSimple("ERROR", ex.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }   
}
