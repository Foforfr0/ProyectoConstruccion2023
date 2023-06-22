/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.EntregaActividadDAO;
import DAO.UsuarioDAO;
import POJO.Curso;
import POJO.EntregaActividad;
import Utils.ShowMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ModificarEntregaController implements Initializable {

    private Label lbNombreAct;
    @FXML
    private TextFlow textAct;
    @FXML
    private TextArea textDescripcion;
    @FXML
    private Pane panel;
    
    private EntregaActividad entrega;
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
        volver();
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
            int idActividad = this.entrega.getIdActividad();
            String descripcion = textDescripcion.getText();
            
            //Crear una nueva entrega
            EntregaActividad nuevaEntrega = new EntregaActividad();
            nuevaEntrega.setIdActividad(idActividad);
            nuevaEntrega.setDescripcion(descripcion);
            nuevaEntrega.setIdUsuario(usuarioActual);
            nuevaEntrega.setFechaEntrega(nuevaEntrega.getFechaEntrega());
            nuevaEntrega.setArchivo(contenidoArchivo);
            nuevaEntrega.setNombreArchivo(nombreArchivo);
            nuevaEntrega.setIdEntregaActividad(entrega.getIdActividad());
            actualizarEntrega(nuevaEntrega);
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnAdjuntarArchivo(ActionEvent event) {
    }
    
    public void prepaparEntrega(EntregaActividad entrega){
        if (entrega != null) {
            this.entrega = entrega;
            this.textDescripcion.setText(entrega.getDescripcion());
            //Convertir a File
            File archivo = null;
            if (archivo != null) {
                try {
                    archivo = File.createTempFile("archivo", null);
                    Files.write(archivo.toPath(), entrega.getArchivo(), StandardOpenOption.CREATE);
                    //mostrarArchivo();
                    this.archivo = archivo;
                    this.nombreArchivo = entrega.getNombreArchivo();
                    mostrarArchivo();
                    System.out.println(entrega.getNombreArchivo());
                    System.out.println("Se paso una entrega");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                
            }
            this.archivo = archivo;
            this.nombreArchivo = entrega.getNombreArchivo();
            mostrarArchivo();
            System.out.println(entrega.getNombreArchivo());
            System.out.println("Se paso una entrega");
        } else {
            System.out.println("No se paso una entrega");
        }
    }
    
    public void ponerDescripcionAct(String descripcion){
        String texto = descripcion;
        Text text = new Text(texto);
        textAct.getChildren().add(text);
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
    
    private void actualizarEntrega(EntregaActividad entrega) throws SQLException{
        ResultadoOperacion resultado = EntregaActividadDAO.modificarEntrega(entrega);
        if (!resultado.isError()) {
            ShowMessage.showAlertSimple("Actividad",
                    "Entrega modificada",
                    Alert.AlertType.INFORMATION);
            volver();
            
        } else {
            ShowMessage.showAlertSimple("ERROR",
                    "No se pudo modificar la entrega",
                    Alert.AlertType.ERROR); 
        }
    }
    
    private void volver(){
        try{
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/ActividadesEstudiante.fxml"));
            Parent root = loader.load();
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            
            ActividadesEstudianteController controlador = loader.getController();
            controlador.ponerDatos(curso);
            //controlador.cargarActividades();
            //controlador.cargarLista(Curso);

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage)this.lbNombreAct.getScene().getWindow();
            currentStage.close();
        }catch(IOException ioe){
            ShowMessage.showAlertSimple(
                "Error", 
                "Error al abrir la ventana", 
                Alert.AlertType.ERROR
            );
        }
    }
    
    public void PonerDatos(Curso curso){
        this.curso = curso;
    }
    
}
