/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.EntregaActividadDAO;
import DAO.UsuarioDAO;
import POJO.Actividad;
import POJO.Curso;
import POJO.EntregaActividad;
import Utils.ShowMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class EntregarActividadController implements Initializable {

    @FXML
    private Label lbNombreAct;
    @FXML
    private TextFlow textAct;
    @FXML
    private TextArea textDescripcion;
    @FXML
    private Pane panel;
    
    private Actividad actividad;
    private EntregaActividad EntregaActividad;
    private File archivo;
    private String nombreArchivo;
    private int usuarioActual = UsuarioDAO.getUsuarioActual().getIdUsuario();
    private Curso curso;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnVolver(ActionEvent event) {
    }

    @FXML
    private void btnEntregarAct(ActionEvent event) {
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
            int idActividad = this.actividad.getIdActividad();
            String descripcion = textDescripcion.getText();
            
            //Crear una nueva entrega
            EntregaActividad nuevaEntrega = new EntregaActividad();
            nuevaEntrega.setIdActividad(idActividad);
            nuevaEntrega.setDescripcion(descripcion);
            nuevaEntrega.setIdUsuario(usuarioActual);
            nuevaEntrega.setFechaEntrega(nuevaEntrega.getFechaEntrega());
            nuevaEntrega.setArchivo(contenidoArchivo);
            nuevaEntrega.setNombreArchivo(nombreArchivo);
            crearEntregaActividad(nuevaEntrega);
        } catch (Exception e) {
        }
    }
    
    private void crearEntregaActividad(EntregaActividad entregaActividad) throws SQLException{
        try {
            ResultadoOperacion resultado = EntregaActividadDAO.crearEntregaActividad(entregaActividad);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Actividad creada",
                        "La entrega se realizó con exito",
                        Alert.AlertType.INFORMATION);
            } else {
                ShowMessage.showAlertSimple("ERROR",
                        "No se entregó la actividad",
                        Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ponerDatos(Actividad actividad){
        lbNombreAct.setText(actividad.getNombre());
        String descripcion = actividad.getDescripcion();
        Text textoDescripcion = new Text(descripcion);
        textAct.getChildren().add(textoDescripcion);
        this.actividad = actividad;
    }
    
    public void ponerCurso(Curso curso){
        this.curso = curso;
    }

    private void xd(ActionEvent event) {
        System.out.println(actividad.getCurso());
        System.out.println(actividad.getIdActividad());
    }

    @FXML
    private void btnAdjuntarArchivo(ActionEvent event) {
        FileChooser dialogoPDF = new FileChooser();
        dialogoPDF.setTitle("Selecciona un archivo");
        Stage escenarioActual = (Stage) lbNombreAct.getScene().getWindow();
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
                    .getResource("/Views/ActividadesEstudiante.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
            
            ActividadesEstudianteController controlador = loader.getController();
            controlador.ponerDatos(curso);
            
            //cerrarVentana();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
}
