/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.ActividadDAO;
import POJO.Actividad;
import POJO.Curso;
import Utils.ShowMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class CrearActividadController implements Initializable {

    @FXML
    private Button idBtnCancelar;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private TextField tfValor;
    @FXML
    private Pane panel; 
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaCierre;
    
    private File archivo;
    private String nombreArchivo;
    private Curso curso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tfValor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfValor.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }    

    @FXML
    private void btnCancelar(ActionEvent event) {
        try {
            ShowMessage.showAlertSimple("Error",
                    "Creacion de actividad cancelada",
                    Alert.AlertType.INFORMATION);
            regresarActividades();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void btnCrearActividad(ActionEvent event) {
        try {
            byte[] contenidoArchivo = null;
            if (archivo != null && archivo.exists()) {
                try (FileInputStream fis = new FileInputStream(archivo);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                    contenidoArchivo = bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }   
            }
            String nombreActividad = tfNombre.getText();
            String descripcion = tfDescripcion.getText();
            String valorString = tfValor.getText();
            int valor = Integer.parseInt(valorString);
            Date fechaInicio = Date.valueOf(dpFechaInicio.getValue());
            Date fechaCierre = Date.valueOf(dpFechaCierre.getValue());
            
            //Crear nueva actividad 
            Actividad actividad = new Actividad();
            actividad.setNombre(nombreActividad);
            actividad.setDescripcion(descripcion);
            actividad.setValor(valor);
            actividad.setFechaInicio(fechaInicio);
            actividad.setFechaCierre(fechaCierre);
            actividad.setCurso(curso.getIdcurso());
            actividad.setArchivo(contenidoArchivo);
            actividad.setNombreArchivo(nombreArchivo);
            registrarActividad(actividad);
        } catch (NullPointerException n) {
            ShowMessage.showAlertSimple("Error",
                    "Faltan datos", 
                    Alert.AlertType.ERROR);
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void registrarActividad(Actividad actividad) throws SQLException{
        try {
            ResultadoOperacion resultado = ActividadDAO.crearActividad(actividad);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Actividad creada",
                        "La actividad se creó con exito",
                        Alert.AlertType.INFORMATION);
                regresarActividades();
            } else {
                ShowMessage.showAlertSimple("ERROR",
                        "ERROR",
                        Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
        }
    }
    
    private void crearActividad() throws SQLException{
        try {
            //ResultadoOperacion resultado = 
        } catch (Exception e) {
        }
    }
    
    @FXML
    private void btnAdjuntarArchivo(ActionEvent event) {
        FileChooser dialogoPDF = new FileChooser();
        dialogoPDF.setTitle("Selecciona un archivo");
        Stage escenarioActual = (Stage) idBtnCancelar.getScene().getWindow();
        File nuevoArchivoPDF = dialogoPDF.showOpenDialog(escenarioActual);

        if (nuevoArchivoPDF != null && nuevoArchivoPDF.exists()) {
            // Eliminar el archivo PDF anterior si existe
            if (archivo != null && archivo.exists()) {
            archivo.delete();
            }

            // Asignar el nuevo archivo PDF adjuntado
            archivo = nuevoArchivoPDF;

            try {
                // Limpiar el paneContenedor antes de mostrar el nuevo archivo adjuntado
                panel.getChildren().clear();

                // Mostrar el icono del pdf y el nombre del archivo
                String rutaImagen = "/Imagenes/archivoIcono.png"; // Reemplaza con la ruta correcta del icono del PDF
                ImageView vistaImagen = new ImageView(rutaImagen);
                vistaImagen.setFitWidth(100);
                vistaImagen.setFitHeight(100);

                Label labelNombreArchivo = new Label(archivo.getName());
                this.nombreArchivo = archivo.getName();
                labelNombreArchivo.setGraphic(vistaImagen);

                panel.getChildren().addAll(vistaImagen, labelNombreArchivo);
            } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }
    
    private void regresarActividades(){
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
            
            cerrarVentana();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    } 
    
    public void ponerDatos(Curso curso){
        this.curso = curso;
    }
    
    private void cerrarVentana(){
        Stage x = (Stage)idBtnCancelar.getScene().getWindow();
        x.close();
    }

    @FXML
    private void btnID(ActionEvent event) {
        System.out.println(curso.getIdcurso());
    }
}
