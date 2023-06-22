package Controllers;

import DAO.EntregaAvanceDAO;
import POJO.Avance;
import POJO.Curso;
import POJO.Usuario;
import POJO.EntregaAvance;
import Utils.ShowMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Comparator;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;


public class CalificarAvancesController implements Initializable {
    @FXML
    private TableView<Usuario> tvEstudiantes;
    @FXML
    private TableColumn<Usuario, Integer> ctMatricula;
    @FXML
    private TableColumn<Usuario, String> ctNombre;
    @FXML
    private TableColumn<Usuario, String> ctApellidoPat;
    @FXML
    private TextArea tfDescripcionEntrega;
    @FXML
    private ImageView ivArchivo;
    @FXML
    private Button btAdjuntar;
    @FXML
    private Button btCalificar2;
    @FXML
    private Button btCalificar;
    @FXML
    private Label lbNombreAlumno;
    @FXML
    private Label lbFechaEntrega;
    @FXML
    private Label lbNombreArchivo;
    @FXML
    private TextField tfCalificacion;
    @FXML
    private Button btCancelar;
    
    public static Curso cursoActual = null;
    public static Avance avanceActual = null;
    private ObservableList<Usuario> listaEstudiantes = null;
    private ObservableList<EntregaAvance> entregasEstud = null;
    private EntregaAvance entregaActual = null;
    private File archivoEntregaActual = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.btCalificar2.setVisible(false);
        this.btCalificar2.setDisable(true);
        this.configurarTabla();
        this.cargarEntregas();
        this.tvEstudiantes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Usuario usuarioSelected = this.tvEstudiantes.getSelectionModel().getSelectedItem();
                
                if (usuarioSelected != null) {
                    try {
                        EntregaAvanceDAO.getEntregaEstudiante(usuarioSelected.getIdUsuario(),avanceActual.getIdAvance());
                        this.entregaActual = EntregaAvanceDAO.entregaActual;
                        
                        this.lbNombreAlumno.setText("Alumno: "+this.entregaActual.getNombreEstudiante());
                        this.lbFechaEntrega.setText("Fecha de entrega: "+entregaActual.getFechaEntrega());
                        this.tfDescripcionEntrega.setText(this.entregaActual.getDescripEntrega());
                        this.tfCalificacion.setText(String.valueOf(this.entregaActual.getCalificacion()));
                        if(this.entregaActual.getArchivo() != null && this.entregaActual.getArchivo().length>0){
                            try{
                                this.lbNombreArchivo.setText(this.entregaActual.getNombreArchivo());

                                File temp = Files.createTempFile("temp", "").toFile();

                                FileOutputStream fos = new FileOutputStream(temp);
                                fos.write(this.entregaActual.getArchivo());
                                fos.close();

                                this.archivoEntregaActual = temp;

                                temp.delete();
                            }catch(IOException ioe){
                                ShowMessage.showAlertSimple(
                                    "Error", 
                                    "Error al recuperar archivo de la base de datos.",
                                    Alert.AlertType.ERROR
                                );
                            }
                            this.ivArchivo.setVisible(true);
                            this.ivArchivo.setDisable(false);
                        }else{
                            this.lbNombreArchivo.setText("");
                            this.ivArchivo.setVisible(false);
                            this.ivArchivo.setDisable(true);
                        }
                    } catch (SQLException ex) {
                        ShowMessage.showAlertSimple(
                            "Error", 
                            "Error de conxión.", 
                            Alert.AlertType.ERROR
                        );
                    }
                }
            }
        });
        // Crear un TextFormatter con un filtro de texto y un convertidor
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change; // Permitir valor vacío
            }

            if (newText.matches("[0-9]|10")) {
                return change; // Permitir números entre 0 y 10
            }

            return null; // Rechazar otros valores
        });
        this.tfCalificacion.setTextFormatter(textFormatter);
    }    
    
    @FXML
    private void clicDescargar(MouseEvent event) {
        // Crear un cuadro de diálogo de selección de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar destino");
        fileChooser.setInitialFileName(this.lbNombreArchivo.getText());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos .PDF o .ZIP", "*.pdf", "*.zip"));
        File file = fileChooser.showSaveDialog((Stage)this.btCalificar.getScene().getWindow());

        if (file != null) {
            String rutaDestino = file.getAbsolutePath();

            try {
                // Descargar el archivo
                FileOutputStream outputStream = new FileOutputStream(rutaDestino);
                /*outputStream.write(avanceElegido.getArchivo());
                outputStream.close();*/
                byte[] archivoBytes = entregaActual.getArchivo();
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
    private void clicAdjuntar(ActionEvent event) {
    }

    @FXML
    private void clicCalificar(ActionEvent event) {
        Usuario estudianteCalif = this.verificarEstudianteSelected();
        
        if(estudianteCalif != null){
            this.btCalificar.setDisable(true);
            this.tfCalificacion.setEditable(true);
            this.btCalificar2.setVisible(true);
            this.btCalificar2.setDisable(false);
            this.btCancelar.setVisible(true);
            this.btCancelar.setDisable(false);
        }else{
            ShowMessage.showAlertSimple(
                "Selección obligatoria", 
                "Debes seleccionar algún registro de la tabla para su modificación", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    @FXML
    private void confirmarCalificacion(ActionEvent event) {
        try{
            EntregaAvanceDAO.calificarAvance(
                this.entregaActual.getIdEntregaAvance(),
                Integer.parseInt(this.tfCalificacion.getText())
            );
        } catch(SQLException sqle){
            ShowMessage.showAlertSimple(
                "Error", 
                "No fué posible asignar una calificación a la entrega.", 
                Alert.AlertType.ERROR
            );
        }
        this.tfCalificacion.setText("");
        this.tfDescripcionEntrega.setText("");
        this.btCalificar2.setDisable(true);
        this.btCalificar2.setVisible(false);
        this.lbFechaEntrega.setText("Fecha de entrega: ");
        this.lbNombreAlumno.setText("Seleccione un alumno");
        this.lbNombreArchivo.setText("");
        this.ivArchivo.setVisible(false);
        this.ivArchivo.setDisable(true);
        this.btCancelar.setVisible(false);
        this.btCancelar.setDisable(true);
        this.btCalificar.setDisable(false);
        ShowMessage.showAlertSimple(
            "Información", 
            "Se ha asignado una calificación a la entrega.", 
            Alert.AlertType.INFORMATION
        );
        this.cargarEntregas();
    }
    
    @FXML
    private void clicVolver(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConsultarAvancesProfesor.fxml"));
            Parent root = loader.load();
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage)this.tvEstudiantes.getScene().getWindow();
            currentStage.close();
        }catch(IOException ioe){
            ShowMessage.showAlertSimple(
                "Error", 
                "Error al abrir la ventana", 
                Alert.AlertType.ERROR
            );
        }
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) throws SQLException {
        this.tfCalificacion.setText("");
        this.tfDescripcionEntrega.setText("");
        this.btCalificar2.setDisable(true);
        this.btCalificar2.setVisible(false);
        this.lbFechaEntrega.setText("Fecha de entrega: ");
        this.lbNombreAlumno.setText("Seleccione un alumno");
        this.lbNombreArchivo.setText("");
        this.ivArchivo.setVisible(false);
        this.ivArchivo.setDisable(true);
        this.btCancelar.setVisible(false);
        this.btCancelar.setDisable(true);
        this.btCalificar.setDisable(false);
        this.cargarEntregas();
    }
    
    private void configurarTabla(){
        this.ctMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        this.ctNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.ctApellidoPat.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
    }
    
    private void cargarEntregas(){         
        try{
            this.entregasEstud = null;
            this.listaEstudiantes = null;
            
            EntregaAvanceDAO.getAlumnos(avanceActual.getIdAvance());
            
            this.listaEstudiantes = FXCollections.observableArrayList(EntregaAvanceDAO.listaAlumnosEntregas);
            this.tvEstudiantes.setItems(this.listaEstudiantes);
        }catch(SQLException sqle){
            ShowMessage.showAlertSimple(
                "Error", 
                "Error de conxión", 
                Alert.AlertType.ERROR
            );
        }
        
        this.ctMatricula.setComparator(Comparator.comparingInt(Integer::intValue));
        this.tvEstudiantes.getSortOrder().add(this.ctMatricula);
    }
    
    private Usuario verificarEstudianteSelected(){
        int selectedRow = this.tvEstudiantes.getSelectionModel().getSelectedIndex();
        return (selectedRow >= 0) ? this.listaEstudiantes.get(selectedRow) : null ;
    }
}
