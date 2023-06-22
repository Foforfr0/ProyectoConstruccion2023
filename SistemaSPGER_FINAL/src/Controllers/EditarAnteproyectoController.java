package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.AnteproyectoDAO;
import DAO.LGACDAO;
import DAO.UsuarioDAO;
import POJO.Anteproyecto;
import POJO.LGAC;
import POJO.Usuario;
import Utils.ShowMessage;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class EditarAnteproyectoController implements Initializable {

    @FXML
    private TextField tfNombreAnteproyecto;
    @FXML
    private ComboBox cbLGACAnteproyecto;
    @FXML
    private TextArea taLineaInvestigación;
    @FXML
    private ComboBox cbCoDirector;
    @FXML
    private Pane paneContenedor;
    @FXML
    private Label lbNombreArchivo;
    private Anteproyecto anteproyecto;
    private byte[] archivo;
    ObservableList<LGAC> listaLGAC;
    ObservableList<Usuario> listaAcademicos;
    private File archivoPDF;    
    private String nombreArchivo;
    @FXML
    private Button idBtnGuardar;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarListaAcademicos();
        cargarListaLGAC();
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
            if (archivoPDF != null && archivoPDF.exists()) {
            archivoPDF.delete();
            }
            // Asignar el nuevo archivo PDF adjuntado
            archivoPDF = nuevoArchivoPDF;
            try {
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

    @FXML
    private void btnCrear(ActionEvent event) throws SQLException {
        try {
            byte[] contenidoArchivo = null;
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
            }
            Usuario usuarioActual = UsuarioDAO.getUsuarioActual();
            String nombreAnteproyecto = tfNombreAnteproyecto.getText();
            String lineaInvestigacion = taLineaInvestigación.getText();
            int estado = 1;
            LGAC idlgac = (LGAC)cbLGACAnteproyecto.getValue();
            int lgacSeleccionada = idlgac.getIdLGAC();
            Usuario codirector = (Usuario)cbCoDirector.getValue();
            int codirectorSeleccionado = codirector.getIdUsuario();
            String nombreCodirector= codirector.getNombre();
            //Modificar anteproyecto existente
            this.anteproyecto.setNombre(nombreAnteproyecto);
            this.anteproyecto.setLineaInvestigacion(lineaInvestigacion);
            this.anteproyecto.setEstado(estado);
            this.anteproyecto.setIdLGAC(lgacSeleccionada);
            this.anteproyecto.setIdDirector(usuarioActual.getIdUsuario());
            this.anteproyecto.setIdCoDirector(codirectorSeleccionado);
            this.anteproyecto.setNombreCoDirector(nombreCodirector);
            this.anteproyecto.setNombreDirector(usuarioActual.getNombre());
            this.anteproyecto.setNombreArchivo(nombreArchivo);
            this.anteproyecto.setArchivo(this.archivo);
            actualizarAnteproyecto(anteproyecto);
            irConsultarAnteproyectos();
        } catch (NullPointerException e) {
            e.printStackTrace();
            ShowMessage.showAlertSimple("Error",
                    "No se pudo guardar el anteproyecto", 
                    Alert.AlertType.ERROR);
        }
    }
    private void cerrarVentana(){
        Stage escenario = (Stage) tfNombreAnteproyecto.getScene().getWindow();
        escenario.close();
    }
    @FXML
    private void btnCancelar(ActionEvent event) {
        ShowMessage.showAlertSimple("Cancelar edición",
                "Edición cancelada",
                Alert.AlertType.INFORMATION);
        irConsultarAnteproyectos();
    }

    
    private void irConsultarAnteproyectos(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/Views/ConsultarAnteproyectos.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);
            Stage escenarioBase = (Stage) idBtnGuardar.getScene().getWindow();
            escenarioBase.setScene(escena);
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    void prepararAnteproyecto(Anteproyecto anteproyecto) {
        this.anteproyecto=anteproyecto;
        this.tfNombreAnteproyecto.setText(anteproyecto.getNombre());
        this.taLineaInvestigación.setText(anteproyecto.getLineaInvestigacion());
        this.archivo=anteproyecto.getArchivo();
        this.nombreArchivo=anteproyecto.getNombreArchivo();
        for(int i=0;i<listaLGAC.size();i++){
            if(listaLGAC.get(i).getIdLGAC()==anteproyecto.getIdLGAC())
                cbLGACAnteproyecto.setValue(listaLGAC.get(i));
        }
        for(int i=0;i<listaAcademicos.size();i++){
            if(listaAcademicos.get(i).getIdUsuario()==anteproyecto.getIdDirector())
                cbCoDirector.setValue(listaAcademicos.get(i));
        }        
        mostrarArchivoSeleccionado();
        
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
    private void mostrarArchivoSeleccionado() {
                String rutaImagen = "/Imagenes/PDFIcono.png"; // Reemplaza con la ruta correcta del icono del PDF
                ImageView vistaImagen = new ImageView(rutaImagen);
                vistaImagen.setFitWidth(100);
                vistaImagen.setFitHeight(100);
                Label labelNombreArchivo = new Label(this.nombreArchivo);
                labelNombreArchivo.setGraphic(vistaImagen);
                paneContenedor.getChildren().addAll(vistaImagen, labelNombreArchivo); 
    }

    private int actualizarAnteproyecto(Anteproyecto nuevoAnteproyecto) throws SQLException {
        int idAnteproyecto = -1; // 
        ResultadoOperacion resultadoPostularAnteproyecto = AnteproyectoDAO
                .actualizarAnteproyecto(nuevoAnteproyecto);
        if (!resultadoPostularAnteproyecto.isError()) {
            idAnteproyecto = resultadoPostularAnteproyecto.getFilasAfectadas();
            ShowMessage.showAlertSimple("Mensaje",
                    "Anteproyecto actualizado"
                    , Alert.AlertType.INFORMATION);
        } else {
            ShowMessage.showAlertSimple("Error", "Campos faltantes"
                    , Alert.AlertType.ERROR);
        }
        return idAnteproyecto;
    }
}
