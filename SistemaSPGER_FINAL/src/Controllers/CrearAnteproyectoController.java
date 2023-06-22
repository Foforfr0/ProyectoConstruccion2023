/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import POJO.LGAC;
import POJO.Usuario;
import DAO.LGACDAO;
import DAO.UsuarioDAO;
import POJO.Anteproyecto;
import ConexionBD.ResultadoOperacion;
import Utils.ShowMessage;
import DAO.AnteproyectoDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class CrearAnteproyectoController implements Initializable {

    @FXML
    private TextField tfNombreAnteproyecto;
    @FXML
    private ComboBox cbLGACAnteproyecto = new ComboBox();
    @FXML
    private TextArea taLineaInvestigación;
    @FXML
    private ComboBox cbCoDirector = new ComboBox();
    @FXML
    private Pane paneContenedor;
    @FXML
    private Label lbNombreArchivo;
    private String nombreArchivo;
    ObservableList<LGAC> listaLGAC;
    ObservableList<Usuario> listaAcademicos;
    private File archivoPDF;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaLGAC();
        cargarListaAcademicos();
        cbCoDirector.getSelectionModel().selectFirst();
        cbLGACAnteproyecto.getSelectionModel().selectFirst();
    }    

    @FXML
    private void btnAdjuntarPDF(ActionEvent event) {
        FileChooser dialogoPDF = new FileChooser();
        dialogoPDF.setTitle("Selecciona un PDF");
        FileChooser.ExtensionFilter filtroPDF = new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf");
        dialogoPDF.getExtensionFilters().add(filtroPDF);
        Stage escenarioActual = (Stage) tfNombreAnteproyecto.getScene().getWindow();
        File nuevoArchivoPDF = dialogoPDF.showOpenDialog(escenarioActual);

        if (nuevoArchivoPDF != null && nuevoArchivoPDF.exists()) {
            // Eliminar el archivo PDF anterior si existe
            if (archivoPDF != null && archivoPDF.exists()) {
            archivoPDF.delete();
            }

            // Asignar el nuevo archivo PDF adjuntado
            archivoPDF = nuevoArchivoPDF;

            try {
                // Limpiar el paneContenedor antes de mostrar el nuevo archivo adjuntado
                paneContenedor.getChildren().clear();

                // Mostrar el icono del pdf y el nombre del archivo
                String rutaImagen = "/Imagenes/PDFIcono.png"; // Reemplaza con la ruta correcta del icono del PDF
                ImageView vistaImagen = new ImageView(rutaImagen);
                vistaImagen.setFitWidth(100);
                vistaImagen.setFitHeight(100);

                Label labelNombreArchivo = new Label(archivoPDF.getName());
                this.nombreArchivo=archivoPDF.getName();
                labelNombreArchivo.setGraphic(vistaImagen);

                paneContenedor.getChildren().addAll(vistaImagen, labelNombreArchivo);
            } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }
    
    /*
    Estados de un anteproyecto
    1 = creado
    2 = postulado
    3 = aceptado
    4 = rechazado
    */    
    private void crearAnteproyecto(Anteproyecto anteproyecto){
        try {
            ResultadoOperacion resultado = AnteproyectoDAO
                    .crearAnteproyecto(anteproyecto);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Crear anteproyecto",
                        resultado.getMensaje(),
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                ShowMessage.showAlertSimple("Crear anteproyecto",
                        resultado.getMensaje(),
                        Alert.AlertType.INFORMATION);
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("")) {
                
            }
        }
    }
    

    private void cargarListaLGAC(){
        listaLGAC = FXCollections.observableArrayList();
        try {
            ArrayList<LGAC> LGACBD = LGACDAO.obtenerLGAC();
            listaLGAC.addAll(LGACBD);
            cbLGACAnteproyecto.setItems(listaLGAC);         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarListaAcademicos(){
        listaAcademicos = FXCollections.observableArrayList();
        try {
            ArrayList<Usuario> academicos = UsuarioDAO.obtenerUsuariosAcademicos();
            listaAcademicos.addAll(academicos);
            cbCoDirector.setItems(listaAcademicos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) throws IOException{
        ShowMessage.showAlertSimple("Aviso",
                "Creación de anteproyecto cancelada",
                Alert.AlertType.INFORMATION);
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
        
    }

    private void getidusuarioactual(ActionEvent event) {
        Usuario usuarioActual = UsuarioDAO.getUsuarioActual();
        System.out.println(usuarioActual.getIdUsuario());
    }


    @FXML
    private void btnCrear(ActionEvent event) {
        String nombreAnteproyecto = tfNombreAnteproyecto.getText().trim();
        String lineaInvestigacion = taLineaInvestigación.getText().trim();
        byte[] contenidoArchivo = null;
        
        if (!nombreAnteproyecto.isEmpty() && !lineaInvestigacion.isEmpty()) {
            try {
                if (archivoPDF != null && archivoPDF.exists()) {
                    try (FileInputStream fis = new FileInputStream(archivoPDF);
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
                } else {
                    ShowMessage.showAlertSimple("Faltan campos",
                            "Agregue un archivo PDF",
                            Alert.AlertType.WARNING);
                }
                Usuario usuario = UsuarioDAO.getUsuarioActual();
                int estado = 1;
                LGAC idLGAC = (LGAC) cbLGACAnteproyecto.getValue();
                int lgacSeleccionada = idLGAC.getIdLGAC();
                Usuario codirector = (Usuario)cbCoDirector.getValue();
                int codirectorSeleccionado = codirector.getIdUsuario();
                String nombreCodirector = codirector.getNombre();
                
                //Crear nuevo anteproyecto
                Anteproyecto nuevoAnteproyecto = new Anteproyecto();
                nuevoAnteproyecto.setNombre(nombreAnteproyecto);
                nuevoAnteproyecto.setLineaInvestigacion(lineaInvestigacion);
                nuevoAnteproyecto.setEstado(estado);
                nuevoAnteproyecto.setIdLGAC(lgacSeleccionada);
                nuevoAnteproyecto.setIdDirector(usuario.getIdUsuario());
                nuevoAnteproyecto.setIdCoDirector(codirectorSeleccionado);
                nuevoAnteproyecto.setNombreCoDirector(nombreCodirector);
                nuevoAnteproyecto.setNombreDirector(usuario.getNombre());
                nuevoAnteproyecto.setNombreArchivo(nombreArchivo);
                nuevoAnteproyecto.setArchivo(contenidoArchivo);
                crearAnteproyecto(nuevoAnteproyecto);
                
            } catch (Exception e) {
            }
            
        } else {
            ShowMessage.showAlertSimple("Faltan campos",
                    "Faltan campos en el formulario",
                    Alert.AlertType.WARNING);
        }  
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) tfNombreAnteproyecto.getScene().getWindow();
        escenario.close();
    }
}
