package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.AvancesDAO;
import DAO.EntregaAvanceDAO;
import POJO.Avance;
import POJO.Usuario;
import POJO.Curso;
import POJO.EntregaAvance;
import Utils.ShowMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConsultarAvancesEstudianteController implements Initializable {

    @FXML
    private TableView<Avance> tvAvances;
    @FXML
    private TableColumn<Avance, Integer> ctNumAvance;
    @FXML
    private TableColumn<Avance, String> ctFechaInicio;
    @FXML
    private TableColumn<Avance, String> ctFechaCierre;
    @FXML
    private Label lbNumAvance;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private ImageView ivArchivoAvance;
    @FXML
    private Label lbNombreArchivoAvance;
    @FXML
    private TextArea tfDescrpEntrega;
    @FXML
    private Label lbFechaEntr;
    @FXML
    private ImageView ivArchivoEntrega;
    @FXML
    private Button btAceptar;
    @FXML
    private Button btCancelar;
    @FXML
    private Button btAdjuntar;
    @FXML
    private Label lbNombreArchivoEntrega;
    @FXML
    private Button btEntregar;
    @FXML
    private Button btModificar;
    @FXML
    private Label lbCalificacion;
    
    public static Usuario estudianteActual = null;
    public static Curso cursoActual = null;
    private static ObservableList<Avance> listaAvances = null;
    private static Avance avanceElegido = null;
    private static EntregaAvance entregaActual = null;
    private static File archivoAvance = null;
    private static File archivoEntrega = null;
    private static boolean esEntrega = false;
    private static boolean esModificacion = false;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.configurarTabla();
        try {
            this.cargarAvances();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarAvancesEstudianteController.class.getName()).log(
                Level.SEVERE, 
                null, 
                ex
            );
        }
        
        this.tvAvances.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Avance selectedAvance = this.tvAvances.getSelectionModel().getSelectedItem();

                if (selectedAvance != null) {
                    /*-------------------CARGAR DATOS DE LA ASIGNACIÓN DEL AVANCE-------------------*/
                    this.avanceElegido = selectedAvance;

                    this.tfDescripcion.setText(selectedAvance.getDescripcion());
                    this.lbNumAvance.setText("Numero de avance: " + String.valueOf(selectedAvance.getNumAvance()));
                    
                    if (selectedAvance.getArchivo() != null && selectedAvance.getArchivo().length > 0) {
                        try {
                            this.lbNombreArchivoAvance.setText(selectedAvance.getNombreArchivo());

                            File temp = Files.createTempFile("temp", "").toFile();

                            FileOutputStream fos = new FileOutputStream(temp);
                            fos.write(selectedAvance.getArchivo());
                            fos.close();

                            this.archivoAvance = temp;

                            temp.delete();
                        } catch (IOException ioe) {
                            ShowMessage.showAlertSimple(
                                "Error",
                                "Error al recuperar archivo de la base de datos.",
                                Alert.AlertType.ERROR
                            );
                        }
                        this.ivArchivoAvance.setVisible(true);
                        this.ivArchivoAvance.setDisable(false);
                    } else {
                        this.lbNombreArchivoAvance.setText("");
                        this.ivArchivoAvance.setVisible(false);
                        this.ivArchivoAvance.setDisable(true);
                    }
                    /*-------------------CARGAR DATOS DE LA ENTREGA DEL ESTUDIANTE-------------------*/
                    try {
                        this.entregaActual = EntregaAvanceDAO.getEntregaEstudiante(
                            this.estudianteActual.getIdUsuario(), 
                            this.avanceElegido.getIdAvance()
                        );
                    } catch (SQLException sqle) {
                        ShowMessage.showAlertSimple(
                            "Error",
                            "Error de conexión con la base de datos.",
                            Alert.AlertType.ERROR
                        );
                    }
                    
                    if (this.entregaActual.getDescripEntrega()!= null && this.entregaActual.getDescripEntrega().length()>0) {
                        this.lbFechaEntr.setText("Fecha de entrega: " + String.valueOf(this.entregaActual.getFechaEntrega()));
                        this.tfDescrpEntrega.setText(this.entregaActual.getDescripEntrega());
                        this.lbCalificacion.setText("Calificación: "+this.entregaActual.getCalificacion());
                        
                        if (this.entregaActual.getArchivo()!= null && this.entregaActual.getArchivo().length>0) {
                            try {
                                this.lbNombreArchivoEntrega.setText(this.entregaActual.getNombreArchivo());

                                File temp = Files.createTempFile("temp", "").toFile();

                                FileOutputStream fos = new FileOutputStream(temp);
                                fos.write(this.entregaActual.getArchivo());
                                fos.close();

                                this.archivoEntrega = temp;

                                temp.delete();
                            } catch (IOException ioe) {
                                ShowMessage.showAlertSimple(
                                    "Error",
                                    "Error al recuperar archivo de la base de datos.",
                                    Alert.AlertType.ERROR
                                );
                            }
                            this.ivArchivoEntrega.setVisible(true);
                            this.ivArchivoEntrega.setDisable(false);
                        } else {
                            this.lbNombreArchivoEntrega.setText("");
                            this.ivArchivoEntrega.setVisible(false);
                            this.ivArchivoEntrega.setDisable(true);
                        }
                    }else{
                        this.lbFechaEntr.setText("Fecha de entrega: ");
                        this.tfDescrpEntrega.setText("");
                        this.lbNombreArchivoEntrega.setText("");
                        this.ivArchivoEntrega.setVisible(false);
                        this.ivArchivoEntrega.setDisable(true);
                    }
                }
            }
            if(avanceElegido.getFechaCierre().before(Date.valueOf(LocalDate.now()))){
                this.btEntregar.setDisable(true);
            }else{
                this.btEntregar.setDisable(false);
            }
        });
    }

    @FXML
    private void clicEntregar(ActionEvent event) {
        Avance avanceSelected = this.verificarAvanceSelected();
        
        if(avanceSelected != null){
            this.esEntrega = !this.esEntrega;     //Habilita el recuadro derecho

            if(this.esEntrega == false){
                habilitarLeftSide();
            }else if(this.esEntrega == true){
                habilitarRightSide();
            } 
        }else{
            ShowMessage.showAlertSimple(
                "Selección obligatoria", 
                "Debes seleccionar un avance para realizar la entrega.", 
                Alert.AlertType.WARNING
            );
        }
    }

    @FXML
    private void clicModificar(ActionEvent event) {
    }
    
    @FXML
    private void clicAdjuntar(ActionEvent event) {
        FileChooser dialogoArchivo = new FileChooser();
        dialogoArchivo.setTitle("Seleccione un archivo");
        FileChooser.ExtensionFilter filtroPDF = new FileChooser.ExtensionFilter("Archivo PDF, RAR O ZIP", "*.pdf", "*.zip", "*.rar");
        dialogoArchivo.getExtensionFilters().add(filtroPDF);
        File nuevoArchivoPDF = dialogoArchivo.showOpenDialog((Stage) this.btAdjuntar.getScene().getWindow());

        if (nuevoArchivoPDF != null && nuevoArchivoPDF.exists()) {
            // Eliminar el archivo PDF anterior si existe
            if (this.archivoEntrega != null && this.archivoEntrega.exists()) {
                this.archivoEntrega.delete();
            }

            // Asignar el nuevo archivo PDF adjuntado
            this.archivoEntrega = nuevoArchivoPDF;
            //nuevoArchivoPDF.delete();
            
            this.lbNombreArchivoEntrega.setText(this.archivoEntrega.getName());
            this.ivArchivoEntrega.setVisible(true);
            this.ivArchivoEntrega.setDisable(false);
        }else{
            this.lbNombreArchivoEntrega.setText("");
            this.ivArchivoEntrega.setVisible(false);
            this.ivArchivoEntrega.setDisable(true);
        }
    }
    
    @FXML
    private void clicDescargarAvance(MouseEvent event) {
        // Crear un cuadro de diálogo de selección de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar destino");
        fileChooser.setInitialFileName(this.lbNombreArchivoAvance.getText());
        File file = fileChooser.showSaveDialog((Stage)this.tvAvances.getScene().getWindow());

        if (file != null) {
            try {
                // Descargar el archivo
                FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                byte[] archivoBytes = this.avanceElegido.getArchivo();
                outputStream.write(archivoBytes);
                outputStream.close();
                ShowMessage.showAlertSimple(
                    "Notificación", 
                    "Se ha descargado correctamente el archivo.", 
                    Alert.AlertType.INFORMATION
                );
            } catch (IOException e) {
                ShowMessage.showAlertSimple(
                    "Error", 
                    "Ocurrió un error al descargar el archivo: " + e.getMessage()+".", 
                    Alert.AlertType.ERROR
                );
            }
        } else {
            ShowMessage.showAlertSimple(
                "Advertencia", 
                "La descarga del archivo ha sido cancelada.", 
                Alert.AlertType.WARNING
            );
        }
    }

    @FXML
    private void clicDescargarEntrega(MouseEvent event) {
        // Crear un cuadro de diálogo de selección de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar destino");
        fileChooser.setInitialFileName(this.lbNombreArchivoEntrega.getText());
        File file = fileChooser.showSaveDialog((Stage)this.tvAvances.getScene().getWindow());
        
        if (file != null) {
            try {
                // Descargar el archivo
                FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                byte[] archivoBytes = this.entregaActual.getArchivo();
                outputStream.write(archivoBytes);
                outputStream.close();
                ShowMessage.showAlertSimple(
                    "Notificación", 
                    "Se ha descargado correctamente el archivo.", 
                    Alert.AlertType.INFORMATION
                );
            } catch (IOException e) {
                ShowMessage.showAlertSimple(
                    "Error", 
                    "Ocurrió un error al descargar el archivo: " + e.getMessage()+".", 
                    Alert.AlertType.ERROR
                );
            }
        } else {
            ShowMessage.showAlertSimple(
                    "Advertencia", 
                    "La descarga del archivo ha sido cancelada.", 
                    Alert.AlertType.WARNING
                );
        }
    }

    @FXML
    private void clicAceptar(ActionEvent event) throws SQLException {
        if(this.esEntrega == true){
            String descripEntrega = this.tfDescrpEntrega.getText();
            if(descripEntrega.isEmpty() || descripEntrega.length()<=30){               //VERIFICA CAMPOS LLENOS
                ShowMessage.showAlertSimple(
                    "Advertencia", 
                    "Favor de escribir una descripción con un mínimo de 30 caracteres.", 
                    Alert.AlertType.WARNING
                );
                return ;
            }
            entregarActividad();
        }
        
        try{
            this.cargarAvances();
        }catch(SQLException sqle){
            throw new SQLException("Error de conexión con la base de datos");
        }
    }
    
    private void entregarActividad(){
        //CREAR TRANSFEROBJECT
        EntregaAvance entregaAvance = new EntregaAvance();
        entregaAvance.setIdAvance(this.avanceElegido.getIdAvance());
        entregaAvance.setIdEstudiante(this.estudianteActual.getIdUsuario());
        entregaAvance.setDescripEntrega(this.tfDescrpEntrega.getText());
            java.sql.Date fecha = java.sql.Date.valueOf(LocalDate.now());               //FECHA DE ENTREGA
        entregaAvance.setFechaEntrega(fecha);
        
        if(this.archivoEntrega!=null && this.archivoEntrega.length()>0){            
            try{
                entregaAvance.setNombreArchivo(this.lbNombreArchivoEntrega.getText());
                FileInputStream fis = new FileInputStream(this.archivoEntrega);
                byte[] fileBytes = new byte[(int) this.archivoEntrega.length()];
                fis.read(fileBytes);

                entregaAvance.setArchivo(fileBytes);
            } catch(IOException ioe){
                ShowMessage.showAlertSimple(
                    "Error", 
                    "No se pude cargar el archivo en la base de datos.", 
                    Alert.AlertType.ERROR
                );
            }
        }
        
        try{
            /*SI NO EXISTE ENTREGA REALIZADA PREVIAMENTE SERÁ FALSE*/
            if(EntregaAvanceDAO.verificarExistenciaEntrega(entregaAvance) == false){
                ResultadoOperacion resultadoEdit = EntregaAvanceDAO.realizarEntrega(entregaAvance);
                if(resultadoEdit.getFilasAfectadas() <= 0){
                    ShowMessage.showAlertSimple(
                        "Error", 
                        "Error de conexión", 
                        Alert.AlertType.ERROR
                    );
                }else{
                    ShowMessage.showAlertSimple(
                        "Información", 
                        "Se ha realizado la entrega del avance.", 
                        Alert.AlertType.INFORMATION
                    );
                    habilitarLeftSide();
                    this.lbFechaEntr.setText("Fecha de entrega: "+entregaAvance.getFechaEntrega());
                    this.lbCalificacion.setText("Calificación: "+entregaAvance.getCalificacion());
                    this.tfDescrpEntrega.setText(entregaAvance.getDescripEntrega());
                    if(entregaAvance.getArchivo()!=null){
                        this.lbNombreArchivoEntrega.setText(entregaAvance.getNombreArchivo());
                        this.ivArchivoEntrega.setVisible(true);
                        this.ivArchivoEntrega.setDisable(false);
                    }
                }
            }else{
                /*SI EXISTE ENTREGA REALIZADA PREVIAMENTE SERÁ TRUE*/
                ResultadoOperacion resultadoEdit = EntregaAvanceDAO.modificarEntrega(entregaAvance);
                if(resultadoEdit.getFilasAfectadas() <= 0){
                    ShowMessage.showAlertSimple(
                        "Error", 
                        "Error de conexión", 
                        Alert.AlertType.ERROR
                    );
                }else{
                    ShowMessage.showAlertSimple(
                        "Información", 
                        "Se ha realizado la modificación de la entrega.", 
                        Alert.AlertType.INFORMATION
                    );
                    habilitarLeftSide();this.lbFechaEntr.setText("Fecha de entrega: "+entregaAvance.getFechaEntrega());
                    this.lbCalificacion.setText("Calificación: "+entregaAvance.getCalificacion());
                    this.tfDescrpEntrega.setText(entregaAvance.getDescripEntrega());
                    if(entregaAvance.getArchivo()!=null){
                        this.lbNombreArchivoEntrega.setText(entregaAvance.getNombreArchivo());
                        this.ivArchivoEntrega.setVisible(true);
                        this.ivArchivoEntrega.setDisable(false);
                    }
                }
            }
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple(
                "Error", 
                "Error de conexión", 
                Alert.AlertType.ERROR
            );
        }
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) throws SQLException {
        ShowMessage.showAlertSimple(
            "Información", 
            "Entrega de actividad cancelada", 
            Alert.AlertType.INFORMATION
        );
        habilitarLeftSide();
        this.lbFechaEntr.setText("Fecha de entrega: ");
        this.lbCalificacion.setText("Calificación: ");
        this.tfDescrpEntrega.setText("");
            this.lbNombreArchivoEntrega.setText("");
            this.ivArchivoEntrega.setVisible(false);
            this.ivArchivoEntrega.setDisable(true);
        try{
            this.cargarAvances();
        }catch(SQLException sqle){
            throw new SQLException("Error de conexión con la base de datos");
        }
    }
    
    @FXML
    private void clicVolver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MenuEstudiante.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage) this.tvAvances.getScene().getWindow();
            currentStage.close();
        } catch (IOException ioe) {
            ShowMessage.showAlertSimple(
                "Error",
                "Error al abrir la ventana",
                Alert.AlertType.ERROR
            );
        }
    }
    
    private void habilitarRightSide(){
        this.tvAvances.setMouseTransparent(true);       //Habilita la selección de la tabla
        this.btEntregar.setDisable(true);               //Habilita los botones Entregar y modificar
        this.btModificar.setDisable(true);              //Para evitar conflicto
        
        this.tfDescrpEntrega.setEditable(true);
        
        this.btAdjuntar.setVisible(true);              //Deshabilita el botón adjuntar, modificar y cancelar
        this.btAdjuntar.setDisable(false);
        this.btAceptar.setVisible(true);
        this.btAceptar.setDisable(false);
        this.btCancelar.setVisible(true);
        this.btCancelar.setDisable(false);
    }
    
    private void habilitarLeftSide(){
        this.tvAvances.setMouseTransparent(false);       //Habilita la selección de la tabla
        this.btEntregar.setDisable(false);               //Habilita los botones Entregar y modificar
        this.btModificar.setDisable(false);              //Para evitar conflicto
        
        this.tfDescrpEntrega.setEditable(false);
        
        this.btAdjuntar.setVisible(false);               //Deshabilita el botón adjuntar, modificar y cancelar
        this.btAdjuntar.setDisable(true);
        this.btAceptar.setVisible(false);
        this.btAceptar.setDisable(true);
        this.btCancelar.setVisible(false);
        this.btCancelar.setDisable(true);
    }
    
    private void configurarTabla() {
        this.ctNumAvance.setCellValueFactory(new PropertyValueFactory("numAvance"));
        this.ctFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        this.ctFechaCierre.setCellValueFactory(new PropertyValueFactory("fechaCierre"));

        this.ctNumAvance.setComparator(Comparator.comparingInt(Integer::intValue));
        this.tvAvances.getSortOrder().add(this.ctNumAvance);
    }

    private void cargarAvances() throws SQLException {
        try {
            AvancesDAO.obtenerAvances(cursoActual);
            listaAvances = FXCollections.observableArrayList(AvancesDAO.avancesConsultadas);
            tvAvances.setItems(this.listaAvances);
        } catch (SQLException sqle) {
            ShowMessage.showAlertSimple(
                "Error",
                "Error de conexión con la base de datos.",
                Alert.AlertType.ERROR
            );
        }

        this.ctNumAvance.setComparator(Comparator.comparingInt(Integer::intValue));
        this.tvAvances.getSortOrder().add(this.ctNumAvance);
    }

    private Avance verificarAvanceSelected() {
        int selectedRow = this.tvAvances.getSelectionModel().getSelectedIndex();
        return (selectedRow >= 0) ? this.listaAvances.get(selectedRow) : null;
    }
}