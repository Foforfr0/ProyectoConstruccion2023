/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
 *
 * @author Carmona
 */
public class ModificarActividadController {

    @FXML
    private Button idBtnCancelar;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private TextField tfValor;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaCierre;
    @FXML
    private Pane panel;
    
    private Actividad actividad;
    private File archivo;
    private String nombreArchivo;
    private int idCurso;
    private Curso Curso;

    @FXML
    private void btnCancelar(ActionEvent event) {
        volver();
    }
    
    private void volver(){
        try{
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/ActividadesDelCurso.fxml"));
            Parent root = loader.load();
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage)this.dpFechaCierre.getScene().getWindow();
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
            actividad.setIdActividad(this.actividad.getIdActividad()); //pasar el id de la actividad
            actividad.setNombre(nombreActividad);
            actividad.setDescripcion(descripcion);
            actividad.setValor(valor);
            actividad.setFechaInicio(fechaInicio);
            actividad.setFechaCierre(fechaCierre);
            actividad.setCurso(idCurso);
            actividad.setArchivo(contenidoArchivo);
            actividad.setNombreArchivo(nombreArchivo);
            actualizarActividad(actividad);
        } catch (NullPointerException n) {
            ShowMessage.showAlertSimple("Error",
                    "Faltan datos", 
                    Alert.AlertType.ERROR);
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
    
    public void prepararActividad(Actividad actividad){
        if (actividad != null) {
            this.actividad = actividad;
            this.tfNombre.setText(actividad.getNombre());
            this.tfDescripcion.setText(actividad.getDescripcion());
            this.idCurso = actividad.getCurso();
            //Convertir a file
            File archivo = null;
            if (archivo != null) {
                try {
                    
                    archivo = File.createTempFile("archivo", null);
                    Files.write(archivo.toPath(), actividad.getArchivo(), StandardOpenOption.CREATE);
                    //mostrarArchivo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 
            this.archivo = archivo;
            this.nombreArchivo = actividad.getNombreArchivo();
            int valor = actividad.getValor();
            String valorString = Integer.toString(valor);
            this.tfValor.setText(valorString);
            mostrarArchivo();
            System.out.println(actividad.getNombreArchivo());
            
            //Poner fecha de inicio en el DatePicker
            if (actividad.getFechaInicio() != null) {
                LocalDate fechaInicio = actividad.getFechaInicio().toLocalDate();
                this.dpFechaInicio.setValue(fechaInicio);
            }
            
            if (actividad.getFechaCierre() != null) {
                LocalDate fechaCierre = actividad.getFechaCierre().toLocalDate();
                this.dpFechaCierre.setValue(fechaCierre);
            }
        } else {
            System.out.println("No hay una actividad en el metodo prepararActividad");
        }
    }
    
    private void mostrarArchivo(){
        String rutaImagen = "/Imagenes/archivoIcono.png";
        ImageView vistaImagen = new ImageView(rutaImagen);
        vistaImagen.setFitWidth(100);
        vistaImagen.setFitHeight(100);
        Label labelNombreArchivo = new Label(this.nombreArchivo);
        labelNombreArchivo.setGraphic(vistaImagen);
        panel.getChildren().addAll(vistaImagen,labelNombreArchivo);
    }
    
    private void actualizarActividad(Actividad actividad) throws SQLException{
        ResultadoOperacion resultado = ActividadDAO.actualizarActividad(actividad);
        if (!resultado.isError()) {
            ShowMessage.showAlertSimple("Actividad",
                    "Actividad modificada",
                    Alert.AlertType.INFORMATION);
            volver();
            
        } else {
            ShowMessage.showAlertSimple("ERROR",
                    "No se pudo modificar la actividad",
                    Alert.AlertType.ERROR); 
        }
    }
    
    public void ponerDatos(Curso curso){
        this.Curso = curso;
    }
}
