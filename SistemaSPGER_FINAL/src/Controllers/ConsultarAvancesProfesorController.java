package Controllers;

import ConexionBD.ResultadoOperacion;
import DAO.AvancesDAO;
import POJO.Avance;
import POJO.Curso;
import Utils.ShowMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.Comparator;
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
import javafx.scene.control.DatePicker;
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


public class ConsultarAvancesProfesorController implements Initializable {

    @FXML
    private TableView<Avance> tvAvances;
    @FXML
    private TableColumn<Avance, Integer> ctNumAvance;
    @FXML
    private TableColumn<Avance, String> ctFechaCierre;
    @FXML
    private TableColumn<Avance, String> ctFechaInicio;
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private TextField tfNumAvance;
    @FXML
    private ImageView ivArchivo;    
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpCierra;
    @FXML
    private Button btCrear;
    @FXML
    private Button btModificar;
    @FXML
    private Button btEliminar;
    @FXML
    private Button btCalificar;
    @FXML
    private Button btAceptar;
    @FXML
    private Button btCancelar;
    @FXML
    private Label lbNombreArchivo;
    @FXML
    private Button btAdjuntar;
    
    private ObservableList<Avance> listaAvances;
    public static Curso cursoActual = null;  
    private static Avance avanceElegido  = null;
    private boolean esEdicion = false;      //Deshabilita el recuadro derecho si son FALSE
    private boolean esCreacion = false;
    private File archivoPDF = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.configurarTabla();
        try {
            this.cargarAvances();
        } catch (SQLException ex) {
            Logger.getLogger(ConsultarAvancesProfesorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.tvAvances.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Avance selectedAvance = this.tvAvances.getSelectionModel().getSelectedItem();
                
                if (selectedAvance != null) {
                    avanceElegido = selectedAvance;
                    
                    // Convertir el objeto java.util.Date a java.time.LocalDate
                    LocalDate fecha = LocalDate.parse(String.valueOf(selectedAvance.getFechaInicio()));
                    this.dpInicio.setValue(fecha);
                    fecha = LocalDate.parse(String.valueOf(selectedAvance.getFechaCierre()));
                    this.dpCierra.setValue(fecha);
                    
                    this.tfDescripcion.setText(selectedAvance.getDescripcion());
                    this.tfNumAvance.setText(String.valueOf(selectedAvance.getNumAvance()));
                    if(selectedAvance.getArchivo() != null && selectedAvance.getArchivo().length>0){
                        try{
                            this.lbNombreArchivo.setText(selectedAvance.getNombreArchivo());
                            
                            File temp = Files.createTempFile("temp", "").toFile();
                            
                            FileOutputStream fos = new FileOutputStream(temp);
                            fos.write(selectedAvance.getArchivo());
                            fos.close();
                            
                            this.archivoPDF = temp;
                            
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
                }
            }
        });
        TextFormatter<Integer> formatoTFNumAvance = new TextFormatter<>(new IntegerStringConverter(), null, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") || newText.isEmpty()) {
                return change;
            }
            return null;
        });
        this.tfNumAvance.setTextFormatter(formatoTFNumAvance);
        this.dpInicio.setEditable(false);
        this.dpCierra.setEditable(false);
    }      
    
    @FXML
    private void clicVolver(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PrincipalAcademico.fxml"));
            Parent root = loader.load();
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual si es necesario
            Stage currentStage = (Stage)this.tvAvances.getScene().getWindow();
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
    private void clicCrear(ActionEvent event) {
        this.esCreacion = !this.esCreacion;     //Habilita el recuadro derecho
        
        if(this.esCreacion == false){
            habilitarLeftSide();
        }else if(this.esCreacion == true){
            habilitarRightSide();
            this.dpInicio.setValue(null);
            this.dpCierra.setValue(null);
            this.tfDescripcion.setText("");
            this.tfNumAvance.setText("");
            this.lbNombreArchivo.setText("");
            this.ivArchivo.setVisible(false);
            this.ivArchivo.setDisable(true);
        }     
    }
    
    @FXML
    private void clicModificar1(ActionEvent event) {
        Avance avanceDelete = this.verificarAvanceSelected();
        
        if(avanceDelete != null){
            this.esEdicion = !this.esEdicion;

            if(this.esEdicion == false){
                habilitarLeftSide();
            }else if(this.esEdicion == true){
                habilitarRightSide();
            }     
        }else{
            ShowMessage.showAlertSimple(
                "Selección obligatoria", 
                "Debes seleccionar algún registro de la tabla para su modificación", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    @FXML
    private void clicAdjuntar(ActionEvent event) {
        FileChooser dialogoPDF = new FileChooser();
        dialogoPDF.setTitle("Seleccione un archivo");
        FileChooser.ExtensionFilter filtroPDF = new FileChooser.ExtensionFilter("Archivo PDF o ZIP", "*.pdf", "*.zip");
        dialogoPDF.getExtensionFilters().add(filtroPDF);
        Stage escenarioActual = (Stage) this.btAdjuntar.getScene().getWindow();
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
                this.ivArchivo.setImage(null);

                // Mostrar el icono del pdf y el nombre del archivo
                String rutaImagen = "/Imagenes/PDFIcono.png"; // Reemplaza con la ruta correcta del icono del PDF
                this.ivArchivo = new ImageView(rutaImagen);

                Label labelNombreArchivo = new Label(archivoPDF.getName());
                this.lbNombreArchivo.setText(archivoPDF.getName());
                labelNombreArchivo.setGraphic(this.ivArchivo);
                this.ivArchivo.setVisible(true);
                this.ivArchivo.setDisable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(this.archivoPDF != null){
            this.ivArchivo = new ImageView("/Imagenes/PDFIcono.png");
        }
    }

    @FXML
    private void clicDescargar(MouseEvent event) {
        // Crear un cuadro de diálogo de selección de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar destino");
        fileChooser.setInitialFileName(this.lbNombreArchivo.getText());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos .PDF o .ZIP", "*.pdf", "*.zip"));
        File file = fileChooser.showSaveDialog((Stage)this.tvAvances.getScene().getWindow());

        if (file != null) {
            String rutaDestino = file.getAbsolutePath();

            try {
                // Descargar el archivo
                FileOutputStream outputStream = new FileOutputStream(rutaDestino);
                /*outputStream.write(avanceElegido.getArchivo());
                outputStream.close();*/
                byte[] archivoBytes = avanceElegido.getArchivo();
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
    private void clicEliminar(ActionEvent event) throws SQLException {
        Avance avanceDelete = this.verificarAvanceSelected();
        
        if(avanceDelete != null){
            boolean eliminar = ShowMessage.showConfirmationDialog(
                "Eliminar avance", 
                "¿Deseas eliminar la información del avance "+avanceDelete.toString()+"?"
            );
            if(eliminar == true){
                try {
                    ResultadoOperacion resultado = AvancesDAO.deleteAvance(avanceDelete.getIdAvance());
                    
                    if(!resultado.isError()){
                        ShowMessage.showAlertSimple(
                            "Registro eliminado", 
                            resultado.getMensaje(), 
                            Alert.AlertType.INFORMATION
                        );
                        this.cargarAvances();
                    }else{
                        ShowMessage.showAlertSimple(
                            "Operación incorrecta", 
                            resultado.getMensaje(), 
                            Alert.AlertType.ERROR
                        );
                    }
                } catch (SQLException e) {
                    ShowMessage.showAlertSimple(
                        "ERROR", 
                        e.getMessage(), 
                        Alert.AlertType.ERROR
                    );
                }
            }
        }else{
            ShowMessage.showAlertSimple(
                "Selección obligatoria", 
                "Debes seleccionar algún registro de la tabla para su eliminación", 
                Alert.AlertType.WARNING
            );
        }
        try{
            this.cargarAvances();
        }catch(SQLException sqle){
            throw new SQLException("Error de conexión con la base de datos");
        }
    }
    
    @FXML
    private void clicAceptar(ActionEvent event) throws SQLException {
        if(this.esEdicion == true){
            if(verificarCamposLlenos() == false){               //VERIFICA CAMPOS LLENOS
                ShowMessage.showAlertSimple(
                    "Advertencia", 
                    "Favor de ingresar los campos faltantes para poder crear la actividad.", 
                    Alert.AlertType.WARNING
                );
                return ;
            }
            if(verificarCampos("Modificacion") == false){
                return ;
            } 
            modificarActividad();
        }
        if(this.esCreacion == true){
            if(verificarCamposLlenos() == false){               //VERIFICA CAMPOS LLENOS
                ShowMessage.showAlertSimple(
                    "Advertencia", 
                    "Favor de ingresar los campos faltantes para poder crear la actividad.", 
                    Alert.AlertType.WARNING
                );
                return ;
            }
            if(verificarCampos("Creacion") == false){
                return ;
            } 
            crearActividad();
        }
        try{
            this.cargarAvances();
        }catch(SQLException sqle){
            throw new SQLException("Error de conexión con la base de datos");
        }
    }
    
    private void crearActividad(){                  
        //SE CREA EL TRANSFEROBJECT
        Avance avanceNew = new Avance();
        avanceNew.setNumAvance(Integer.parseInt(this.tfNumAvance.getText()));    //NUMERO DE AVANCE
        avanceNew.setDescripcion(this.tfDescripcion.getText());                   //DESCRIPCION
            java.sql.Date fecha = java.sql.Date.valueOf(LocalDate.now());               //FECHA DE CREACION
        avanceNew.setFechaCreacion(fecha);
            fecha = java.sql.Date.valueOf(this.dpInicio.getValue());                    //FECHA DE INICIO
        avanceNew.setFechaInicio(fecha);
            fecha = java.sql.Date.valueOf(this.dpCierra.getValue());                    //FECHA DE CIERRE
        avanceNew.setFechaCierre(fecha);
        avanceNew.setCurso(AvancesDAO.cursoActual.getIdcurso());                        //CURSO AL QUE PERTENECE
        avanceNew.setNombreArchivo(this.lbNombreArchivo.getText());             //NOMBRE DE ARCHIVO Y ARCHIVO
        try{
            FileInputStream fis = new FileInputStream(this.archivoPDF);
            byte[] fileBytes = new byte[(int) this.archivoPDF.length()];
            fis.read(fileBytes);
            
            avanceNew.setArchivo(fileBytes);
        } catch(IOException ioe){
            ShowMessage.showAlertSimple(
                "Error", 
                "No se pude cargar el archivo en la base de datos.", 
                Alert.AlertType.ERROR
            );
        }
        
        try {
            ResultadoOperacion resultadoEdit = AvancesDAO.createAvance(avanceNew, this.cursoActual);
            if(resultadoEdit.getFilasAfectadas() <= 0){
                ShowMessage.showAlertSimple(
                    "Error", 
                    "Error de conexión", 
                    Alert.AlertType.ERROR
                );
            }else{
                habilitarLeftSide();
            }
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple(
                "Error", 
                "Error de conexión", 
                Alert.AlertType.ERROR
            );
        }
    }
    
    private void modificarActividad(){
        if(this.validarExistenciaAvance(this.tfNumAvance.getText(), cursoActual) == true){   //Busca la existencia de datos faltantes o incorrectos
            //SE CREA EL TRANSFEROBJECT
            Avance avanceEdit = new Avance();
            avanceEdit.setIdAvance(this.verificarAvanceSelected().getIdAvance());
                java.sql.Date date = java.sql.Date.valueOf(this.dpInicio.getValue());
            avanceEdit.setFechaInicio(date);
                date = java.sql.Date.valueOf(this.dpCierra.getValue());
            avanceEdit.setFechaCierre(date);
            date = null;
            avanceEdit.setDescripcion(this.tfDescripcion.getText());
            avanceEdit.setNumAvance(Integer.parseInt(this.tfNumAvance.getText()));
            avanceEdit.setNombreArchivo(this.lbNombreArchivo.getText());
            try{
                FileInputStream fis = new FileInputStream(this.archivoPDF);
                byte[] fileBytes = new byte[(int) this.archivoPDF.length()];
                fis.read(fileBytes);

                avanceEdit.setArchivo(fileBytes);
            } catch(IOException ioe){
                ShowMessage.showAlertSimple(
                    "Error", 
                    "No se pude cargar el archivo en la base de datos.", 
                    Alert.AlertType.ERROR
                );
            }
            
            
            try {
                ResultadoOperacion resultadoNew = AvancesDAO.updateAvance(avanceEdit);
                if(resultadoNew.getFilasAfectadas() <= 0){
                    ShowMessage.showAlertSimple(
                        "Error", 
                        "Error de conexión", 
                        Alert.AlertType.ERROR
                    );
                }else{
                    habilitarLeftSide();
                }
            } catch (SQLException ex) {
                ShowMessage.showAlertSimple(
                    "Error", 
                    "Error de conexión", 
                    Alert.AlertType.ERROR
                );
            }
        }else{
            ShowMessage.showAlertSimple(
                "Error", 
                "Ha ingresado datos incorrectos", 
                Alert.AlertType.ERROR
            );
        }
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) throws SQLException {
        habilitarLeftSide();
        try{
            this.cargarAvances();
        }catch(SQLException sqle){
            throw new SQLException("Error de conexión con la base de datos");
        }
    }
    
    @FXML
    private void clicCalificar(ActionEvent event) {
        Avance avanceCalificar = this.verificarAvanceSelected();
        
        if(avanceCalificar != null){
            try{
                CalificarAvancesController.cursoActual = cursoActual;
                CalificarAvancesController.avanceActual = avanceElegido;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/CalificarAvances.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                // Cerrar la ventana actual si es necesario
                Stage currentStage = (Stage)this.tvAvances.getScene().getWindow();
                currentStage.close();
            }catch(IOException ioe){
                ShowMessage.showAlertSimple(
                    "Error", 
                    "Error al abrir la ventana.", 
                    Alert.AlertType.ERROR
                );
            }
        }else{
            ShowMessage.showAlertSimple(
                "Selección obligatoria", 
                "Debes seleccionar algún registro de la tabla para calificar.", 
                Alert.AlertType.WARNING
            );
        }
    }
    
    private boolean verificarCamposLlenos(){
        String numAvance = this.tfNumAvance.getText();
        String descripcion = this.tfDescripcion.getText();
        
        if(numAvance.isEmpty() || descripcion.isEmpty() ||
           this.dpInicio.getValue() == null || this.dpCierra.getValue() == null){
            return false;
        }else{
            return true;
        }
    }
    
    private boolean verificarCampos(String tipoValidacion){
        String descripcion = this.tfDescripcion.getText();  //VERIFICA TAMAÑO DE LA DESCRIPCIÓN
        if(descripcion.length() <= 30){
            ShowMessage.showAlertSimple(
                "Advertencia", 
                "Descripción muy corta.\nFavor de escribir una descripción mayor a 30 caracteres.", 
                Alert.AlertType.WARNING
            );
            return false;
        }
        if(tipoValidacion == "Creacion"){
            if(this.dpInicio.getValue().isBefore(LocalDate.now())){    //VALIDA SELECCIÓN DE FECHA DE INICIO ANTERIOR A LA ACTUAL
                ShowMessage.showAlertSimple(
                    "Error", 
                    "Ha seleccionado una fecha inicio anterior a la fecha actual: "+LocalDate.now()+".", 
                    Alert.AlertType.ERROR
                );
                return false;
            }
        }
        if(this.dpCierra.getValue().isEqual(this.dpInicio.getValue())){    //VALIDA SELECCIÓN DE FECHA DE CIERRE IGUAL A INICIO
            ShowMessage.showAlertSimple(
                "Error", 
                "Ha seleccionado una fecha inicio igual a la fecha de cierre.", 
                Alert.AlertType.ERROR
            );
            return false;
        }
        if(this.dpCierra.getValue().isBefore(this.dpInicio.getValue())){    //VALIDA SELECCIÓN DE FECHA DE CIERRE ANTERIOR A INICIO
            ShowMessage.showAlertSimple(
                "Error", 
                "Ha seleccionado una fecha de cierre anterior a la fecha de inicio.", 
                Alert.AlertType.ERROR
            );
            return false;
        }
        if(tipoValidacion == "Creacion"){
            try{                                                //VALIDA QUE NO EXISTA AVANCE CON EL MISMO NUMERO
                int numeroAvance = Integer.parseInt(this.tfNumAvance.getText());
                if(AvancesDAO.validarNumAvance(numeroAvance, cursoActual) == false){
                    ShowMessage.showAlertSimple(
                        "Error", 
                        "Ya existe un avance con el mismo numero.", 
                        Alert.AlertType.ERROR
                    );
                    return false;
                }
            }catch (SQLException ex) {
                ShowMessage.showAlertSimple(
                    "Error", 
                    "No se pudo validar la existencia del nuevo avance en la base de datos.", 
                    Alert.AlertType.ERROR
                );
                return false;
            }
        }
        return true;    //No hay errores
    }
    
    private boolean validarExistenciaAvance(String numAvance, Curso cursoAct){
        try {  
            int numeroAvance = Integer.parseInt(this.tfNumAvance.getText());
            if(AvancesDAO.validarNumAvance(numeroAvance, cursoAct) == true){
                return true;
            }
        } catch (SQLException ex) {
            ShowMessage.showAlertSimple(
                "Error", 
                "Error en la conexión", 
                Alert.AlertType.ERROR
            );
            return false;
        }finally{
            return true;
        }
    }
    
    private void habilitarRightSide(){
        this.tvAvances.setMouseTransparent(true);  //Deshabilita la selección de la tabla
        this.btCrear.setDisable(true);             //Deshabilita los botones crear, modificar y eliminar
        this.btModificar.setDisable(true);         //Para evitar conflicto
        this.btEliminar.setDisable(true);
        this.btCalificar.setDisable(true);
        this.dpInicio.setEditable(true);           //Habilita los DatePicker y el TextField
        this.dpCierra.setEditable(true);
        this.tfDescripcion.setEditable(true);
        this.tfNumAvance.setEditable(true);
        this.btAdjuntar.setVisible(true);          //Habilita el botón adjuntar, modificar y cancelar
            this.btAdjuntar.setDisable(false);
        this.btAceptar.setVisible(true);
            this.btAceptar.setDisable(false);
        this.btCancelar.setVisible(true);
            this.btCancelar.setDisable(false);
    }
    
    private void habilitarLeftSide(){
        this.tvAvances.setMouseTransparent(false);  //Habilita la selección de la tabla
        this.btCrear.setDisable(false);             //Habilita los botones crear, modificar y eliminar
        this.btModificar.setDisable(false);         //Para evitar conflicto
        this.btEliminar.setDisable(false);
        this.btCalificar.setDisable(false);
        this.dpInicio.setEditable(false);           //Deshabilita los DatePicker y el TextField
        this.dpCierra.setEditable(false);
        this.tfDescripcion.setEditable(false);
        this.tfNumAvance.setEditable(false);
        this.btAdjuntar.setVisible(false);          //Deshabilita el botón adjuntar, modificar y cancelar
        this.btAdjuntar.setDisable(true);
        this.btAceptar.setVisible(false);
        this.btAceptar.setDisable(true);
        this.btCancelar.setVisible(false);
        this.btCancelar.setDisable(true);
    }
    
    private void configurarTabla(){
        this.ctNumAvance.setCellValueFactory(new PropertyValueFactory("numAvance"));
        this.ctFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        this.ctFechaCierre.setCellValueFactory(new PropertyValueFactory("fechaCierre"));
        
        this.ctNumAvance.setComparator(Comparator.comparingInt(Integer::intValue));
        this.tvAvances.getSortOrder().add(this.ctNumAvance);
    }
    
    private void cargarAvances() throws SQLException{   
        try{
            this.listaAvances = null;
            AvancesDAO.obtenerAvances(this.cursoActual);
            this.listaAvances = FXCollections.observableArrayList(AvancesDAO.avancesConsultadas);
            tvAvances.setItems(this.listaAvances);
        }catch(SQLException sqle){
            throw new SQLException("Error de conexión con la base de datos");
        }
        
        this.ctNumAvance.setComparator(Comparator.comparingInt(Integer::intValue));
        this.tvAvances.getSortOrder().add(this.ctNumAvance);
    }
    
    private Avance verificarAvanceSelected(){
        int selectedRow = this.tvAvances.getSelectionModel().getSelectedIndex();
        return (selectedRow >= 0) ? this.listaAvances.get(selectedRow) : null ;
    }
    
    public static void setCursoActual(Curso curso){
        cursoActual = curso;
    }
}